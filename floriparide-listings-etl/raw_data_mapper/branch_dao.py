import psycopg2
import psycopg2.extras
__author__ = 'Mike'


def get_all():
    """
    """
    result = []

    conn = None
    cur = None
    try:
        conn = psycopg2.connect("dbname=floriparide_listings user=postgres password=postgres host=localhost port=5432")
        cur = conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
        query = "SELECT * FROM public.branch as b"
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


