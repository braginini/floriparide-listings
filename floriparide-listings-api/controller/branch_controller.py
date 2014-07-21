import bottle
__author__ = 'Mike'

app = bottle.Bottle()


@app.get("/search")
def search():
    return "Hello World!"