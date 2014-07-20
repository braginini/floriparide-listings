import psycopg2
import psycopg2.extras
__author__ = 'mikhail'


def load_timestamps():
    """
    loads the last run timestamp along with current timestamp of the DB
    :return: timestamp with timezone
    """

    conn = None
    cur = None
    try:
        conn = psycopg2.connect("dbname=floriparide_listings user=postgres password=postgres host=localhost port=5432")
        cur = conn.cursor()
        query = "SELECT timestamp, now() FROM audit.index_builder"
        print("Running query %s" % query)
        cur.execute(query)
        return cur.fetchone()
    finally:
        if cur is not None:
            cur.close()
        if conn is not None:
            conn.close()


def update_timestamp(timestamp):
    """
    """

    conn = None
    cur = None
    try:
        conn = psycopg2.connect("dbname=floriparide_listings user=postgres password=postgres host=localhost port=5432")
        cur = conn.cursor()
        query = "UPDATE audit.index_builder SET timestamp = %s" % str(timestamp)
        print("Running query %s" % query)
        cur.execute(query)
        return cur.fetchone()
    finally:
        if cur is not None:
            cur.close()
        if conn is not None:
            conn.close()


def get_history(ts, audit_table):
    """
    selects all fields from specified audit_table that have timestamp >= specified one
    :param ts: timestamp with timezone
    :param audit_table: the audit table with schema specified
    :return: the set of rows specified audit_table
    """

    conn = None
    cur = None
    try:
        conn = psycopg2.connect("dbname=floriparide_listings user=postgres password=postgres host=localhost port=5432")
        cur = conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
        query = "SELECT * FROM %s WHERE timestamp >= '%s'" % (audit_table, ts)
        print("Running query %s" % query)
        cur.execute(query)
        return cur.fetchall()
    finally:
        if cur is not None:
            cur.close()
        if conn is not None:
            conn.close()

