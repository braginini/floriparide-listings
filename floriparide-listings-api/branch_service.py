import logging
from operator import itemgetter
import datetime
from elasticsearch import Elasticsearch
import cache
import config
import dao

__author__ = 'mikhail'

es = Elasticsearch(hosts=['%s:%s' % (config.ES.HOST, config.ES.PORT)])
# es = Elasticsearch(hosts=['localhost:9200'])

branch_dao = dao.branch_dao
rubric_dao = dao.rubric_dao
attribute_dao = dao.attribute_dao


def populate_cache(els, el_cache):
    for e in els:
        el_cache.put(e['id'], e)


populate_cache(branch_dao.get_full(0), cache.branch_cache)
populate_cache(attribute_dao.get_entity(), cache.attribute_cache)
# add hardcoded attributes
cache.attribute_cache.put('visible', dict(data={'input_type': 'bounds'}))
cache.attribute_cache.put('open', dict(data={'input_type': 'boolean'}))

open_filter_script = '_source.get(\'schedule\') is not None and _source.get(\'schedule\').get(day) ' \
                     'is not None ' \
                     'and len([r for r in _source.get(\'schedule\').get(day) if r[\'from\'] < hour < r[\'to\']]) > 0'


def is_number(s):
    try:
        float(s)
        return True
    except ValueError:
        return False


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


def build_sort(sort):
    """
    creates ES sort object
    :param sort:
    :return:
    """
    sort_obj = []
    if sort:
        for k, v in sort.items():
            if k == 'raiting' or k == 'rating':
                sort_obj.append({'rating': 'desc'})
            else:
                sort_obj.append({k: 'desc'})

    return sort_obj



def build_filters(filters):
    """
    takes filters map and returns a elastic search ready filter
    :param filters:
    :return:
    """

    root_filter = []

    if filters:
        for k, v in filters.items():
            if is_number(k):
                attr = cache.attribute_cache.get(int(k))
            else:
                attr = cache.attribute_cache.get(str(k))

            # todo do a check for hard coded filters
            if attr:
                t = attr['data']['input_type']
                if t == 'boolean' and type(v) is bool and v:
                    if k == 'open':
                        now = datetime.datetime.now()
                        params = dict(day=str(now.weekday()), hour=now.hour + now.minute / 60)
                        open_filter = dict(script=open_filter_script, params=params, lang='python')
                        root_filter.append(dict(script=open_filter))
                    else:
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
                if t == 'bounds' and type(v) is list and v and len(v) == 4 and k == 'visible':
                    location = dict(top_left={'lat': v[0], 'lon': v[1]}, bottom_right={'lat': v[2], 'lon': v[3]})
                    geo_bounding_box = dict(point=location)
                    root_filter.append(dict(geo_bounding_box=geo_bounding_box))

    return root_filter


def search(q, project_id, start, limit, filters=None, sort=None):
    root_filter = build_filters(filters)
    sort_obj = build_sort(sort)

    # todo add dynamic locale specific query fields (e.g. rubrics.names.pt_Br applied if pt_Br arrives)
    filtered = {'query': {
        'multi_match': {
            'fields': ['name',
                       'address.street',
                       'address.additional',
                       'address.neighborhood',
                       'payment_options',
                       'rubrics.names.pt_Br^3',
                       'attributes.names.pt_Br^3',
                       'tags',
                       'headline.'
                       'description'],
            'query': q
        }
    }}

    if root_filter:
        filtered['filter'] = {'and': root_filter}

    body = {
        "from": start,
        "query": {
            "filtered": filtered
        }
    }

    if sort_obj:
        body['sort'] = sort_obj

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

    if not sort:
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
    general_attrs = {}
    slider_attr = {}
    for ag in attribute_groups:
        attributes = attribute_dao.get_attributes(ag['id'])
        ag['attributes'] = attributes
        if ag['general']:
            for a in attributes:
                if a['filter_type'] == 'slider':
                    if not slider_attr or slider_attr['weight'] < a.get('weight', 0):
                        general_attrs.pop(slider_attr.get('id'), None)
                        slider_attr = dict(id=a['id'], weight=a.get('weight', 0))
                        general_attrs[a['id']] = a
                else:
                    general_attrs[a['id']] = a

    attribute_groups = [ag for ag in attribute_groups if not ag.get('general', None)]
    # for ag in attribute_groups:
    #     for a in ag['attributes']:
    #         if a['id'] in general_attrs:
    #             general_attrs.pop(a['id'])

    if attribute_groups:
        attribute_groups.insert(0, dict(attributes=list(general_attrs.values())))

    return top_rubrics, attribute_groups


def get_list(project_id, company_id=None, rubric_id=None, start=None, limit=None, filters=None, sort=None):
    """
    gets the list of branch for the given project and company
    :param project_id: the project to search in
    :param company_id: the company to search in
    :return:
    """

    root_filter = build_filters(filters)
    sort_obj = build_sort(sort)

    if rubric_id:
        root_filter.append({
            "term": {
                "rubrics.id": rubric_id
            }
        })

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


def get_rubrics(project_id):
    return rubric_dao.get_list(project_id)