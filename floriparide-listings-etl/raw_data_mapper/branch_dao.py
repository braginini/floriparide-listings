import psycopg2
import base_dao
__author__ = 'Mike'


def get_all():
    return base_dao("public.branch", cursor_factory=psycopg2.extras.RealDictCursor)

def get_all_full():
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

    for b in result:
        b["rubrics"] = get_branch_rubrics(b["id"])
        return result

    return result


def get_branch_rubrics(branch_id):
    """
    """
    result = []
    conn = None
    cur = None
    try:
        conn = psycopg2.connect("dbname=floriparide_listings user=postgres password=postgres host=localhost port=5432")
        cur = conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
        query = "SELECT r.id FROM rubric r, branch_rubrics br WHERE r.id = br.rubric_id AND br.branch_id = %s" % branch_id
        print("Running query %s" % query)
        cur.execute(query)
        result = cur.fetchall()
    finally:
        if cur is not None:
            cur.close()
        if conn is not None:
            conn.close()

    return result


def get_by_attrs_rubrics(attribute_ids=None, rubric_ids=None, exclude_ids=None):
    """
    """
    if not attribute_ids and not rubric_ids:
        return

    conn = None
    cur = None
    try:
        conn = psycopg2.connect("dbname=floriparide_listings user=postgres password=postgres host=localhost port=5432")
        cur = conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor)

        query = "SELECT b.* FROM branch b, " \
                        "json_array_elements((data->>'attributes')::json) as a, " \
                        "json_array_elements((data->>'rubrics')::json) as r"

        if exclude_ids:
            query += " WHERE id NOT IN (%s)" % ",".join(str(x) for x in exclude_ids)

        if attribute_ids:
            if exclude_ids:
                query += " AND"
            else:
                query += " WHERE"
            query += " (a->>'id')::bigint IN (%s)" % ",".join(str(x) for x in attribute_ids)

        if rubric_ids:
            if attribute_ids:
                query += " OR"
            elif exclude_ids:
                query += " AND"
            else:
                query += " WHERE"
            query += " (r->>'id')::bigint IN (%s)" % ",".join(str(x) for x in rubric_ids)

        print("Running query %s" % query)
        cur.execute(query)
        return cur.fetchall()
    finally:
        if cur is not None:
            cur.close()
        if conn is not None:
            conn.close()


def enum(**enums):
    return type('Enum', (), enums)


