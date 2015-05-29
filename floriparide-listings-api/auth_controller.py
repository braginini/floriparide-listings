import json
import random
import bottle
import branch_service
import service
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


@app.post("/feedback")
@json_response
@enable_cors
@validate(project_id=int)
def feedback(project_id):
    """
    :return:
    """
    name = bottle.request.forms.get('name')
    email = bottle.request.forms.get('email')
    body = bottle.request.forms.get('body')
    rating = bottle.request.forms.get('rating')
    service.save_feedback(project_id, name, email, body, rating)
