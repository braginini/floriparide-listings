from contextlib import contextmanager
import psycopg2
from psycopg2 import pool
from psycopg2 import extras


__author__ = 'Mike'

connection_pool = pool.ThreadedConnectionPool(1, 20, dbname="floriparide_listings", user="postgres",
                                              password="postgres",
                                              host="localhost", port="5432")


def get_branches(branch_ids):
    """
    get all the branches by specified ids
    :param branch_ids: the list of branch ids to return
    :return:
    """
    with get_cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cur:
        query = "SELECT * FROM public.branch as b WHERE b.id in (%s)" % ",".join(branch_ids)
        print("Running query %s" % query)
        cur.execute(query)
        return cur.fetchall()


@contextmanager
def get_cursor(cursor_factory=None):
    con = connection_pool.getconn()
    try:
        if cursor_factory:
            yield con.cursor(cursor_factory=cursor_factory)
        else:
            yield con.cursor()
    finally:
        connection_pool.putconn(con)
