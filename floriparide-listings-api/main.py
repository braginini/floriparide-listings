import logging
import os
import bottle
import auth_controller
import config
from bottle import run

import branch_controller
import project_controller
import rubric_controller

__author__ = 'Mikhail Bragin'

path = os.path.dirname(os.path.abspath(__file__))

#logging.basicConfig(level=logging.INFO)
logFormatter = logging.Formatter("%(asctime)s [%(threadName)-12.12s] [%(levelname)-5.5s]  %(message)s")
rootLogger = logging.getLogger()
rootLogger.setLevel(level=logging.INFO)

fileHandler = logging.FileHandler("{0}/{1}.log".format(path, 'api'))
fileHandler.setFormatter(logFormatter)
rootLogger.addHandler(fileHandler)

logging.info('Starting server...')
root_app = bottle.Bottle()
root_app.mount("/catalog/1.0/branch", branch_controller.app)
root_app.mount("/catalog/1.0/project", project_controller.app)
root_app.mount("/catalog/1.0/rubric", rubric_controller.app)
root_app.mount("/catalog/1.0/auth", auth_controller.app)

if __name__ == '__main__':
    run(app=root_app, host=config.WEB.HOST, port=config.WEB.PORT, debug=True)
