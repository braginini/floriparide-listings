import bottle

import controller


__author__ = 'Mikhail Bragin'

root_app = bottle.Bottle()
root_app.mount("/catalog/1.0/", controller.app)

if __name__ == '__main__':
    bottle.run(app=root_app, host='localhost', port=1234, debug=True)
