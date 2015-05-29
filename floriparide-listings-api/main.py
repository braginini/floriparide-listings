import logging
import bottle
import auth_controller
import config

import branch_controller
import project_controller
import rubric_controller

__author__ = 'Mikhail Bragin'

logging.basicConfig(level=logging.INFO)

root_app = bottle.Bottle()
root_app.mount("/catalog/1.0/branch", branch_controller.app)
root_app.mount("/catalog/1.0/project", project_controller.app)
root_app.mount("/catalog/1.0/rubric", rubric_controller.app)
root_app.mount("/catalog/1.0/auth", auth_controller.app)

if __name__ == '__main__':
    bottle.run(app=root_app, host=config.WEB.HOST, port=config.WEB.PORT, debug=True)
