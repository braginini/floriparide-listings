
import bottle
from util.controller_utils import validate, json_response, enable_cors
from beaker.cache import cache_managers


app = bottle.Bottle()


def localize_names(obj, locale):
    """
    obj should contain key "names"
    """
    if obj:
        if not obj.get('data').get('names'):
            return
        obj['name'] = obj['data']['names'].get(locale)
        obj.pop('data', None)
        return obj


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
