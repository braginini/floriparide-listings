import bottle
import dao
import service
from util.controller_utils import validate, json_response, enable_cors


app = bottle.Bottle()


@app.get("/branch/<project_id:int>/<id:int>")
@json_response
@enable_cors
def branch_get(project_id, id):
    branches = dao.get_branches_full([str(id)])
    return branches


@app.get("/branch/search")
@json_response
@enable_cors
@validate(q=str, project_id=int, start=int, limit=int, locale=str, attrs=str)
def branch_search(q, project_id, start, limit, locale="pt_Br", attrs=None):
    # todo get index name from db by project id
    # todo get default locale by project id

    result = {}
    branches, total = service.branch_search(q, project_id, start, limit, attrs)
       # prepare markers with branch_id, name, lat, lon
    if start == 0:
        markers = [dict(branch_id=b["id"],
                        name=b["name"],
                        lat=b["data"]["geometry"]["point"]["lat"],
                        lon=b["data"]["geometry"]["point"]["lon"])
                   for b in branches if b["data"].get("geometry")]
        top_rubrics = service.get_top_rubrics(branches)
        result["markers"] = markers
        result["top_rubrics"] = top_rubrics

    # cut the resulting list. Only after we get markers and top rubrics!!!
    # Cuz markers and top rubrics are calculated based on full search result
    if limit:
        if limit > total:
            limit = total
        branches = branches[:limit]

    result["items"] = branch_response(branches, locale)
    result["total"] = total

    return result


def branch_response(branches, locale):
    """
    prepares branches to be sent to client
    :param branches:
    :return:
    """

    #localize attributes and rubrics by specified locale
    def localize(not_localized):
        if not_localized:
            return [dict(id=a["id"], name=a["data"]["names"].get(locale)) for a in not_localized]

    return [dict(id=b["id"],
                 name=b["name"],
                 attributes=localize(b["attributes"]),
                 rubrics=localize(b["rubrics"]),
                 address=b["data"].get("address"),
                 geometry=b["data"].get("geometry"))
            for b in branches]

# todo get full branch from dao
# todo cache attributes and rubrics
