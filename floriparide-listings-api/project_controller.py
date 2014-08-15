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

    projects = project_service.get_list()
    if not projects:
        raise bottle.HTTPError(status=404,
                               body="No projects were found")

    projects = [project_response(p, locale) for p in projects]

    return {"items": projects, "total": len(projects)}


def project_response(project, locale):
    return dict(id=project["id"],
                name=project["data"]["names"].get(locale),
                locale=project["data"]["locale"],
                language=project["data"]["language"],
                time_zone=project["data"]["time_zone"],
                zoom=project["data"]["zoom"],
                bounds=project["data"]["bounds"],
                default_position=project["data"]["default_position"])
