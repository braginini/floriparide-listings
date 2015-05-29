import json
import random
import bottle
import branch_service
from util.controller_utils import validate, json_response, enable_cors


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


@app.get("/list")
@json_response
@enable_cors
@validate(locale=str, project_id=int)
def get_list(project_id, locale='pt_Br'):
    """
    :return:
    """
    result = {}
    rubrics = branch_service.get_rubrics(project_id)

    result['total'] = len(rubrics)
    rubrics = [localize_names(r, locale) for r in rubrics]
    result['items'] = [dict(id=r['id'],
                            name=r['name'],
                            parent_id=r['parent_id'])
                       for r in rubrics]
    return result