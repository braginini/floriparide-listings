import psycopg2
__author__ = 'mikhail'


def get_all(table_name, cursor_factory=None):
    """
    gets all rows from specified table
    :param table_name: the name of table to scan. Should be with schema specified
    :param cursor_factory: the cursor_factory of a result. The representation of a result
    :return: the list of rows (tuples)
    """
    conn = None
    cur = None
    try:
        conn = psycopg2.connect("dbname=floriparide_listings user=postgres password=postgres host=localhost port=5432")
        if not cursor_factory:
            cur = conn.cursor()
        else:
            cur = conn.cursor(cursor_factory)

        query = "SELECT * FROM %s" % table_name
        print("Running query %s" % query)
        cur.execute(query)
        return cur.fetchall()
    finally:
        if cur is not None:
            cur.close()
        if conn is not None:
            conn.close()

