import logging
from operator import itemgetter
from elasticsearch import Elasticsearch
import dao

__author__ = 'mikhail'

es = Elasticsearch()


def branch_search(q, project_id, start, limit, attrs=None):
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
        body["size"] = 1000
    else:
        body["size"] = limit

    #search in ES
    logging.info("Running ES query %s" % body)
    es_result = es.search(index="florianopolis", doc_type="branch", body=body)
    logging.info("Got ES result for query %s" % body)

    total = es_result["hits"]["total"]

    # get the dictionary of branches with corresponding score (key -> branch_id, value -> score)
    ids = {v["_id"]: v["_score"] for v in es_result["hits"]["hits"]}
    branches = dao.get_branches_full(ids.keys())
    #todo pub score to branches and sort by ES score

    return branches, total


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
        if b["data"].get("rubrics"):
            for r in b["data"]["rubrics"]:
                r_id = r["id"]
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

    return top_rubrics