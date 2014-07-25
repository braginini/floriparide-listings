import json
from bottle import response
import bottle
from util.controller_utils import validate
from elasticsearch import Elasticsearch


app = bottle.Bottle()
es = Elasticsearch()


@app.get("/branch/search")
@validate(q=str, project_id=int, start=int, limit=int, locale=str, attrs=str)
def branch_search(q, project_id, start, limit, locale="pt_Br", attrs=None):
    # todo get index name from db by project id
    # todo get default locale by project id

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

    result_func = None
    if start == 0:
        # we need to take all the results (internal limit is 1k) to be able to return markers and rubrics
        body["size"] = 1000
        result_func = prepare_full_result
    else:
        # do normal request to ES
        body["size"] = limit
        result_func = prepare_result

    es_result = result_func(es.search(index="florianopolis",
                                      doc_type="branch",
                                      body=body), locale)

    response.content_type = "text/plain;charset=UTF8"
    return json.dumps({"result": es_result}, ensure_ascii=False)


def prepare_full_result(es_result, locale):
    """
    prepares result for full output (with markers and rubrics) when start == 0 in paging
    :return:
    """
    result = prepare_result(es_result, locale)

    markers = []
    rubrics = []
    return result


def prepare_result(es_result, locale):
    """
    prepares result for regular output (without markes and rubrics) when start <> 0 in paging
    :param raw_result:
    :return:
    """

    result = {}
    items = [v["_source"] for v in es_result["hits"]["hits"]]
    result["items"] = items
    result["total"] = es_result["hits"]["total"]
    return result
