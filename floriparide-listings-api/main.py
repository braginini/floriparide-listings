import logging
import bottle
import config

import branch_controller
import project_controller

__author__ = 'Mikhail Bragin'

logging.basicConfig(level=logging.INFO)

root_app = bottle.Bottle()
root_app.mount("/catalog/1.0/branch", branch_controller.app)
root_app.mount("/catalog/1.0/project", project_controller.app)

if __name__ == '__main__':
    bottle.run(app=root_app, host=config.WEB.HOST, port=config.WEB.PORT, debug=True)
