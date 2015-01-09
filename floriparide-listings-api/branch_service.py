import logging
from operator import itemgetter
from elasticsearch import Elasticsearch
import dao

__author__ = 'mikhail'

es = Elasticsearch(hosts=['107.170.149.118:9200'])

branch_dao = dao.branch_dao
rubric_dao = dao.rubric_dao


def get(project_id, branch_ids):
    """
    gets the branch by specified project_id and id
    :param project_id:
    :param branch_ids:
    :return:
    """
    return branch_dao.get_full(project_id, branch_ids=branch_ids)


def search(q, project_id, start, limit, attrs=None):
    body = {
        "from": start,
        "query": {
            "filtered": {
                "query": {
                    "match_phrase": {
                        "_all": q
                    }
                }
            }
        }
    }

    if start == 0:
        # we need to take all the results (internal limit is 1k) to be able to return markers and rubrics
        body['size'] = 1000
    else:
        body['size'] = limit

    # search in ES
    logging.info('Running ES query %s' % body)
    #todo remove hardcoded florianopolis index name
    es_result = es.search(index="florianopolis", doc_type='branch', body=body)
    logging.info('Got ES result for query %s' % body)

    total = es_result['hits']['total']
    #dictionary with branch id as a key and score as a value
    es_scores = {e['_id']: e['_score'] for e in es_result['hits']['hits']}

    # get the dictionary of branches with corresponding score (key -> branch_id, value -> score)
    if not total:
        return [], total

    ids = {v['_id']: v['_score'] for v in es_result['hits']['hits']}
    branches = branch_dao.get_full(project_id=project_id, branch_ids=ids.keys())

    branches = sorted(branches, key=lambda branch: es_scores[str(branch['id'])], reverse=True)

    return branches, total


def get_markers(branches):
    """
    builds a set of markers for given list of branches
    :param branches:
    :return:
    """

    def is_paid(branch):
        if branch['draft'].get('paid'):
            return True
        return False

    return [dict(branch_id=b['id'],
                 name=b['name'],
                 lat=b['draft']['geometry']['point']['lat'],
                 lon=b['draft']['geometry']['point']['lng'],
                 paid=is_paid(b))
            for b in branches if b['draft'].get('geometry')]


def get_top_rubrics(branches):
    """
    calculates top rubrics for a given branch list
    top rubric is a rubric that appears in a 30% of branches
    Note, if no rubric found that match 30% threshold - the rubric with the highest count will be returned
    :param branches:
    :return:
    """
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

    #add attribute groups to each rubric
    for r in top_rubrics:
        attributes

    return top_rubrics


def get_list(project_id, company_id=None, rubric_id=None, start=None, limit=None):
    """
    gets the list of branch for the given project and company
    :param project_id: the project to search in
    :param company_id: the company to search in
    :return:
    """
    if not start:
        limit = 1000

    filters = {}
    if company_id:
        filters['company_id'] = company_id
    if rubric_id:
        filters['rubric_id'] = rubric_id

    branches = branch_dao.get_full(project_id, offset=start, limit=limit, filters=filters, order='id')
    total = branch_dao.count(filters=filters)
    return branches, total