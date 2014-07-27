import configparser

__author__ = 'Mike'

parser = configparser.ConfigParser()


class DB():
    HOST = "localhost"
    PORT = 5432
    DB_NAME = "floriparide_listings"
    USER = "postgres"
    PASSWORD = "postgres"
    POOL_MIN_CONN = 1
    POOL_MAX_CONN = 20


class WEB():
    HOST = "localhost"
    PORT = 8888


DB = DB()
WEB = WEB()


def read_config_file(config_file):
    """
    reads a config file and fills up fields
    :return:
    """

    parser.read(config_file)
    DB.HOST = parser.get('db', 'host')
    DB.PORT = parser.get('db', 'port')
    DB.DB_NAME = parser.get('db', 'dbname')
    DB.USER = parser.get('db', 'user')
    DB.PASSWORD = parser.get('db', 'password')
    DB.POOL_MIN_CONN = parser.getint('db', 'pool_min_conn')
    DB.POOL_MAX_CONN = parser.getint('db', 'pool_max_con')

    WEB.HOST = parser.get('web', 'host')
    WEB.PORT = parser.getint('web', 'port')


def get(section, prop):
    return parser.get(section, prop)
