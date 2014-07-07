import psycopg2
import psycopg2.extras
__author__ = 'Mike'
#load timestamp from db of file
#get new_timestamp in ms from DB "SELECT EXTRACT (EPOCH FROM now())"
#do a select to audit table filtering out all entries with timestamp < timestamp
#order entries for each source_id by timestamp and take the last one
#convert to ES index JSON
#send to ES in a batch
#update timestamp in DB or file by new_timestamp


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

old_timestamp, new_timestamp = load_timestamps()
history = get_history(old_timestamp, "audit.a_branch")
pass




