import bottle
from util.controller_utils import validate, json_response, enable_cors


app = bottle.Bottle()


@app.get("/list")
@json_response
@enable_cors
@validate(locale=str)
def get_list(locale="pt_Br"):
    pass