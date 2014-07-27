import json
from operator import itemgetter
from bottle import response
import bottle
import dao
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
                                      body=body), locale, limit)

    response.content_type = "application/json;charset=UTF8"
    response.set_header('Access-Control-Allow-Origin', '*')
    response.set_header('Access-Control-Allow-Methods', 'GET,POST,PUT,DELETE')
    response.set_header('Access-Control-Allow-Headers', '*')
    return json.dumps({"result": es_result}, ensure_ascii=False)


def prepare_full_result(es_result, locale, limit):
    """
    prepares result for full output (with markers and rubrics) when start == 0 in paging
    :return:
    """
    result = prepare_result(es_result, locale, limit)

    branches = dao.get_branches([b["_id"] for b in es_result["hits"]["hits"]])

    # prepare markers with branch_id, name, lat, lon
    markers = [dict(branch_id=b["id"],
                    name=b["name"],
                    lat=b["data"]["geometry"]["point"]["lat"],
                    lon=b["data"]["geometry"]["point"]["lon"])
               for b in branches if b["data"].get("geometry")]

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

    result["markers"] = markers
    result["top_rubrics"] = top_rubrics

    return result


def prepare_result(es_result, locale, limit=None):
    """
    prepares result for regular output (without markes and rubrics) when start <> 0 in paging
    :param es_result:
    :return:
    """

    result = {"total": es_result["hits"]["total"]}

    def prepare_attrs_rubrics(raw):
        if raw:
            return [dict(id=a["id"], name=a["names"].get(locale)) for a in raw]

    # get rubrics and attributes from ES result
    ids = {v["_id"]: dict(attributes=prepare_attrs_rubrics(v["_source"].get("attributes")),
                          rubrics=prepare_attrs_rubrics(v["_source"].get("rubrics")))
           for v in es_result["hits"]["hits"]}
    branches = dao.get_branches(ids.keys())

    if limit:
        if limit > result["total"]:
            limit = result["total"]
        branches = branches[:limit]

    branches = [dict(id=b["id"],
                     name=b["name"],
                     attributes=ids[str(b["id"])].get("attributes"),
                     rubrics=ids[str(b["id"])].get("rubrics"),
                     address=b["data"].get("address"),
                     geometry=b["data"].get("geometry"))
                for b in branches]

    result["items"] = branches

    return result
