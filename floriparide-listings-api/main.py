import getopt
import logging
import bottle
import sys
import config

import controller

__author__ = 'Mikhail Bragin'

logging.basicConfig(level=logging.INFO)


def parse_args(argv):
    config_file = ''
    try:
        opts, args = getopt.getopt(argv, "hc:", ["cFile="])
    except getopt.GetoptError:
        print("usage: main.py -c <configfile>")
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print("usage: main.py -c <configfile>")
            sys.exit()
        elif opt in ("-c", "--cFile"):
            config_file = arg

    print('Config file is "', config_file)
    config.read_config_file(config_file)


#parse_args(sys.argv[1:])

root_app = bottle.Bottle()
root_app.mount("/catalog/1.0/", controller.app)

if __name__ == '__main__':
    bottle.run(app=root_app, host=config.WEB.HOST, port=config.WEB.PORT, debug=True)
