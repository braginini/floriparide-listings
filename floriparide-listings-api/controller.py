import bottle
from bottle import error
import service
from util.controller_utils import HttpException
from util.controller_utils import validate, json_response, enable_cors


app = bottle.Bottle()


@app.get("/branch/list/<project_id:int>/<id:int>")
@json_response
@enable_cors
@validate(locale=str)
def branch_list(project_id, id, locale="pt_Br"):
    # branch will be a list of size 1 if the item was found
    branch = service.get_branch(project_id, id)
    if not branch:
        raise HttpException(status=404, body="No branch was found for given id=%d and project_id=%d" % (id, project_id))

    return {"items": branch_response(branch, locale)}


@app.get("/branch/<project_id:int>/<id:int>")
@json_response
@enable_cors
@validate(locale=str)
def branch_get(project_id, id, locale="pt_Br"):
    # branch will be a list of size 1 if the item was found
    branch = service.get_branch(project_id, id)
    if not branch:
        #raise HttpException(status=404, body="No branch was found for given id=%d and project_id=%d" % (id, project_id))
        raise bottle.HTTPError(status=404,
                               body="No branch was found for given id=%d and project_id=%d" % (id, project_id))

    return {"items": branch_response(branch, locale)}


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
        result["markers"] = service.get_branch_markers(branches)
        result["top_rubrics"] = service.get_branches_top_rubrics(branches)

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

    # localize attributes and rubrics by specified locale
    def localize(not_localized):
        if not_localized:
            return [dict(id=a["id"], name=a["data"]["names"].get(locale)) for a in not_localized]

    def payment_opts(raw_opts):
        if raw_opts:
            return [o["option"] for o in raw_opts]

    return [dict(id=b["id"],
                 name=b["name"],
                 attributes=localize(b["attributes"]),
                 rubrics=localize(b["rubrics"]),
                 address=b["data"].get("address"),
                 contacts=b["data"].get("contacts"),
                 payment_options=payment_opts(b["data"].get("payment_options")),
                 schedule=b["data"].get("schedule"),
                 geometry=b["data"].get("geometry"))
            for b in branches]


@app.error(404)
@json_response
@enable_cors
def error404(error):
    return error