import bottle
from util.controller_utils import check_params


app = bottle.Bottle()


@app.get("/search")
@check_params(q=str, start=int, limit=int, attrs=str)
def search(q, start, limit, attrs=None):
    return "Hello World!"