import bottle
import branch_service
from util.controller_utils import validate, json_response, enable_cors


app = bottle.Bottle()


@app.get("/list")
@json_response
@enable_cors
@validate(locale=str, project_id=int, company_id=int, rubric_id=int, start=int, limit=int)
def get_list(project_id, start, limit, locale="pt_Br", company_id=None, rubric_id=None):
    """
    Gets a set of branches by specified filters - rubric id and company id.
    :param project_id: project id to search branch in. Required
    :param start: the start index of result to return from (paging). Required
    :param limit: the size of results to return (paging). Required
    :param locale: the localization of a result set. Optional (default=pt_Br)
    :param company_id: the id of a company to. Optional (if not specified, rubric_id must be specified)
    :param rubric_id: the id of a rubric to filter out results. Optional (if not, specified company_id must be specified)
    :return:
    """
    # make some validation before
    if not company_id and not rubric_id:
        raise bottle.HTTPError(status=400,
                               body="Either rubric_id or company_id should be specified in request")

    # branch will be a list of size 1 if the item was found
    branches, total = branch_service.get_list(project_id, company_id=company_id, rubric_id=rubric_id, start=start,
                                           limit=limit)

    if not branches:
        raise bottle.HTTPError(status=404,
                               body="No branches were found for given request")

    result = {
        "total": total
    }
    if start == 0:
        result["markers"] = branch_service.get_markers(branches)

    if limit:
        if limit > total:
            limit = total
        branches = branches[:limit]

    result["items"] = branch_response(branches, locale)

    return result


@app.get("/")
@json_response
@enable_cors
@validate(locale=str, project_id=int, id=str)
def get(project_id, id, locale="pt_Br"):
    """
    gets one or more branches by specified id(ids)
    :param project_id: project id to search branch in. Required
    :param id: comma-separated list of branch ids
    :param locale: locale of a result
    :return:
    """
    # branch will be a list of size 1 if the item was found
    branch_ids = id.split(",")
    branches = branch_service.get(project_id, branch_ids)
    if not branches:
        raise bottle.HTTPError(status=404,
                               body="No branches were found for given id=%s and project_id=%d" % (id, project_id))
    return {"items": branch_response(branches, locale), "total": len(branches)}


@app.get("/search")
@json_response
@enable_cors
@validate(q=str, project_id=int, start=int, limit=int, locale=str, attrs=str)
def search(q, project_id, start, limit, locale="pt_Br", attrs=None):
    # todo get index name from db by project id
    # todo get default locale by project id

    result = {}
    branches, total = branch_service.search(q, project_id, start, limit, attrs)
    # prepare markers with branch_id, name, lat, lon
    if start == 0:
        result["markers"] = branch_service.get_markers(branches)
        result["top_rubrics"] = branch_service.get_top_rubrics(branches)

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
