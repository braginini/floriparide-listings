from contextlib import contextmanager
import psycopg2
import psycopg2.extras
from psycopg2 import pool
import config

__author__ = 'Mike'


connection_pool = pool.ThreadedConnectionPool(config.DB.POOL_MIN_CONN,
                                              config.DB.POOL_MAX_CONN,
                                              dbname=config.DB.DB_NAME,
                                              user=config.DB.USER,
                                              password=config.DB.PASSWORD,
                                              host=config.DB.HOST,
                                              port=config.DB.PORT)


class RawData:
    """a class for a RawData model"""
    table = "raw_data.data"
    (CATEGORIES, FACILITIES, PAYMEN_OPTIONS) = ("categories", "facilities", "payment_options")
    (HAGAH, FOUR_SQUARE) = ("hagah", "4square")


def insert(batch, source):
    with get_cursor() as curr:
        to_insert = list()
        query = 'INSERT INTO raw_data.data (source, draft, raw) VALUES (%s, %s::json, %s::json)'
        for line in batch:
            to_insert.append((source, line['draft'], line['raw']))
            if len(to_insert) >= 1000:
                curr.executemany(query, to_insert)
                to_insert.clear()

        if to_insert:
            curr.executemany(query, to_insert)


def get_by_value_list(field, in_list):
    """
    Selects all rows by specified json field
    :param field: field to put in WHERE clause
    :param in_list: a list of values of the field to put in IN clause
    :return: a list of raw data from raw_data.data table
    """
    result = []
    if len(in_list) == 0:
        return result

    conn = None
    cur = None
    try:
        conn = psycopg2.connect("dbname=floriparide_listings user=postgres password=postgres host=localhost port=5432")
        cur = conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
        #small hack for IN clause because JSON fields in PG have '"value"' format
        in_list = ["'\"" + el + "\"'" for el in in_list]
        in_clause = ",".join(in_list)
        query = "SELECT d.* FROM %s as d, json_array_elements(d.data->'%s') AS field WHERE field::text IN (%s)" \
                 % (RawData.table, field, in_clause)
        print("Running query %s" % query)
        cur.execute(query)
        result = cur.fetchall()
    finally:
        if cur is not None:
            cur.close()
        if conn is not None:
            conn.close()

    return result


def enum(**enums):
    return type('Enum', (), enums)


@contextmanager
def get_cursor(cursor_factory=None):
    con = connection_pool.getconn()
    con.autocommit = True
    try:
        if cursor_factory:
            yield con.cursor(cursor_factory=cursor_factory)
        else:
            yield con.cursor()
    finally:
        connection_pool.putconn(con)


