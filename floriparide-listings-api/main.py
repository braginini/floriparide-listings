import logging
import config

import branch_controller
import project_controller

from tornado.ioloop import IOLoop
from tornado.web import Application, url

__author__ = 'Mikhail Bragin'

logging.basicConfig(level=logging.INFO)


# parse_args(sys.argv[1:])

# root_app = bottle.Bottle()
# root_app.mount("/catalog/1.0/branch", branch_controller.app)
# root_app.mount("/catalog/1.0/project", project_controller.app)


def make_app():
    return Application([
        url(r"/catalog/1.0/branch", branch_controller.GetHandler)
    ])


def main():
    app = make_app()
    app.listen(config.WEB.PORT)
    IOLoop.current().start()

if __name__ == '__main__':
    #bottle.run(app=root_app, host=config.WEB.HOST, port=config.WEB.PORT, debug=True)
    main()
