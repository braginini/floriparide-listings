import bottle
import project_service
from util.controller_utils import validate, json_response, enable_cors


app = bottle.Bottle()


@app.get("/list")
@json_response
@enable_cors
@validate(locale=str)
def get_list(locale="pt_Br"):

    if locale:
        locale = locale.lower()

    projects = project_service.get()
    if not projects:
        raise bottle.HTTPError(status=404,
                               body="No projects were found")

    projects = [project_response(p, locale) for p in projects]

    return {"items": projects, "total": len(projects)}


@app.get("/")
@json_response
@enable_cors
@validate(id=str, locale=str)
def get(id, locale="pt_Br"):
    """
    gets a list of projects by specified ids
    :param id: a comma-separated list of ids to return
    :param locale:
    :return:
    """
    if locale:
        locale = locale.lower()
    project_ids = id.split(",")
    projects = project_service.get(project_ids=project_ids)
    if not projects:
        raise bottle.HTTPError(status=404,
                               body="No projects were found for given ids=%s" % id)

    projects = [project_response(p, locale) for p in projects]

    return {"items": projects, "total": len(projects)}


def project_response(project, locale):
    return dict(id=project["id"],
                name=project["data"]["names"].get(locale),
                locale=project["data"]["locale"],
                language=project["data"]["language"],
                time_zone=project["data"]["time_zone"],
                zoom=project["data"]["zoom"],
                #bounds=project["data"]["bounds"],  todo use from DB
                bounds=[[-27.229, -48.088], [-27.982, -48.893]],
                default_position=project["data"]["default_position"])
