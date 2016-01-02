
import bottle
from util.controller_utils import validate, json_response, enable_cors
from beaker.cache import cache_managers
from index_builder import update_index


app = bottle.Bottle()


@app.get("/clear_cache")
@json_response
@enable_cors
def get_list():
    """
    :return:
    """
    names = []
    for ch in cache_managers.values():
        ch.clear()
        names.append(ch.namespace_name)
    return names


@app.get("/update_index")
@json_response
@enable_cors
@validate(project_id=str)
def update_index(project_id):
    """
    :param project_id:
    :return:
    """
    update_index(project_id)
    return "Ok"
