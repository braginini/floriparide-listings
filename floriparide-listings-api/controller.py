import json
from bottle import response
import bottle
from util.controller_utils import validate
from elasticsearch import Elasticsearch


app = bottle.Bottle()
es = Elasticsearch()


@app.get("/branch/search")
@validate(q=str, project_id=int, start=int, limit=int, attrs=str)
def branch_search(q, project_id, start, limit, attrs=None):
    # todo get index name from db by project id

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
        # do normal request to ES
        body["size"] = limit

    es_result = es.search(index="florianopolis",
                          doc_type="branch",
                          body=body)

    result = {}
    items = [v["_source"] for v in es_result["hits"]["hits"]]
    result["items"] = items
    result["total"] = es_result["hits"]["total"]
    response.content_type = "text/plain;charset=UTF8"
    return json.dumps({"result": result}, ensure_ascii=False)