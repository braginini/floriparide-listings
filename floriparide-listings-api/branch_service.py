import logging
from operator import itemgetter
from elasticsearch import Elasticsearch
import cache
import config
import dao

__author__ = 'mikhail'

es = Elasticsearch(hosts=['%s:%s' % (config.ES.HOST, config.ES.PORT)])
#es = Elasticsearch(hosts=['localhost:9200'])

branch_dao = dao.branch_dao
rubric_dao = dao.rubric_dao
attribute_dao = dao.attribute_dao


def populate_cache(els, el_cache):
    for e in els:
        el_cache.put(e['id'], e)


populate_cache(branch_dao.get_full(0), cache.branch_cache)
populate_cache(attribute_dao.get_entity(), cache.attribute_cache)


def get(project_id, branch_ids):
    """
    gets the branch by specified project_id and id
    :param project_id:
    :param branch_ids:
    :return:
    """

    ids = []
    branches = []
    for i in branch_ids:
        cached = cache.branch_cache.get(key=int(i))
        if cached:
            branches.append(cached)
        else:
            ids.append(i)

    if ids:
        branches += branch_dao.get_full(project_id=project_id, branch_ids=ids)

    return branches


def search(q, project_id, start, limit, filters=None):
    root_filter = []

    # todo add attribute filters

    if filters:
        for k, v in filters.items():
            attr = cache.attribute_cache.get(int(k))
            if attr:
                t = attr['data']['input_type']
                if t == 'boolean' and type(v) is bool and v:
                    attr_filter = [{
                                       "term": {
                                           "attributes.id": str(k)
                                       }
                                   }, {
                                       "term": {
                                           "attributes.value": v
                                       }
                                   }]
                    root_filter.append({"and": attr_filter})

    filtered = {"query": {
        "match_phrase": {
            "_all": q
        }
    }}

    if root_filter:
        filtered['filter'] = root_filter

    body = {
        "from": start,
        "query": {
            "filtered": filtered
        }
    }

    if start == 0:
        # we need to take all the results (internal limit is 1k) to be able to return markers and rubrics
        body['size'] = 1000
    else:
        body['size'] = limit

    # search in ES
    logging.info('Running ES query %s' % body)
    # todo remove hardcoded florianopolis index name
    es_result = es.search(index="florianopolis", doc_type='branch', body=body)
    logging.info('Got ES result for query %s' % body)

    total = es_result['hits']['total']
    # dictionary with branch id as a key and score as a value
    es_scores = {e['_id']: e['_score'] for e in es_result['hits']['hits']}

    if not total:
        return [], total

    # get the dictionary of branches with corresponding score (key -> branch_id, value -> score)
    branches = []
    ids = []
    for v in es_result['hits']['hits']:
        cached = cache.branch_cache.get(key=int(v['_id']))
        if cached:
            branches.append(cached)
        else:
            ids.append(v['_id'])
    if ids:
        branches += branch_dao.get_full(project_id=project_id, branch_ids=ids)

    branches = sorted(branches, key=lambda branch: es_scores[str(branch['id'])], reverse=True)

    return branches, total


def get_markers(branches):
    """
    builds a set of markers for given list of branches
    :param branches:
    :return:
    """

    if not branches:
        return {}

    def is_paid(branch):
        if branch['draft'].get('paid'):
            return True
        return False

    return [dict(branch_id=b['id'],
                 name=b['name'],
                 lat=b['draft']['geometry']['point']['lat'],
                 lng=b['draft']['geometry']['point']['lng'],
                 paid=is_paid(b),
                 attributes=b['draft'].get('attributes'))
            for b in branches if b['draft'].get('geometry')]


def get_top_rubrics(branches):
    """
    calculates top rubrics and top attributes for a given branch list
    top rubric is a rubric that appears in a 30% of branches
    Note, if no rubric found that match 30% threshold - the rubric with the highest count will be returned
    :param branches:
    :return:
    """

    if not branches:
        return [], []

    # prepare top rubrics. minimum = 1 rubric and 30% threshold
    # key - id, value number of times appeared
    rubrics = {}
    for b in branches:
        if b['draft'].get('rubrics'):
            for r in b['draft']['rubrics']:
                r_id = r['id']
                if r_id in rubrics:
                    rubrics[r_id] += 1
                else:
                    rubrics[r_id] = 1

    # sort will result in a list of tuples (id, count)
    rubrics = sorted(rubrics.items(), key=itemgetter(1), reverse=True)
    # apply 30% threshold
    top_rubrics = [e[0] for e in rubrics if e[1] > (len(branches) * 0.3)]

    if not top_rubrics and rubrics:
        # take top rubric if no rubric had survived a threshold
        top_rubrics.append(rubrics[0][0])

    attribute_groups = rubric_dao.get_attribute_groups(top_rubrics)
    for ag in attribute_groups:
        attributes = attribute_dao.get_attributes(ag['id'])
        ag['attributes'] = attributes

    return top_rubrics, attribute_groups


def get_list(project_id, company_id=None, rubric_id=None, start=None, limit=None):
    """
    gets the list of branch for the given project and company
    :param project_id: the project to search in
    :param company_id: the company to search in
    :return:
    """

    root_filter = []

    if rubric_id:
        root_filter.append({
            "term": {
                "rubrics.id": rubric_id
            }
        })

    # todo add attribute filters

    body = {
        "from": start,
        "filter": {
            "and": root_filter
        }
    }

    if start == 0:
        # we need to take all the results (internal limit is 1k) to be able to return markers and rubrics
        body['size'] = 1000
    else:
        body['size'] = limit

    es_result = es.search(index="florianopolis", doc_type='branch', body=body)

    total = es_result['hits']['total']
    # dictionary with branch id as a key and score as a value
    es_scores = {e['_id']: e['_score'] for e in es_result['hits']['hits']}

    filters = {}
    if company_id:
        filters['company_id'] = company_id
    if rubric_id:
        filters['rubric_id'] = rubric_id

    if not total:
        return [], total

    # get the dictionary of branches with corresponding score (key -> branch_id, value -> score)
    branches = []
    ids = []
    for v in es_result['hits']['hits']:
        cached = cache.branch_cache.get(key=int(v['_id']))
        if cached:
            branches.append(cached)
        else:
            ids.append(v['_id'])
    if ids:
        branches += branch_dao.get_full(project_id=project_id, branch_ids=ids)

    return branches, total