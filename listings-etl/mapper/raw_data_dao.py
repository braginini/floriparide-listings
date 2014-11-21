import psycopg2
import psycopg2.extras
__author__ = 'Mike'


class RawData:
    """a class for a RawData model"""
    table = "raw_data.data"
    (CATEGORIES, FACILITIES, PAYMEN_OPTIONS) = ("categories", "facilities", "payment_options")
    (HAGAH, FOUR_SQUARE) = ("hagah", "4square")


def get_by_value_list(field, in_list):
    """
    Selects all rows bu specified json field
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


