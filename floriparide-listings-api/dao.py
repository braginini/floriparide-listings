from contextlib import contextmanager
import psycopg2
from psycopg2 import pool
from psycopg2 import extras
import config
import logging


__author__ = 'Mike'

connection_pool = pool.ThreadedConnectionPool(config.DB.POOL_MIN_CONN,
                                              config.DB.POOL_MAX_CONN,
                                              dbname=config.DB.DB_NAME,
                                              user=config.DB.USER,
                                              password=config.DB.PASSWORD,
                                              host=config.DB.HOST,
                                              port=config.DB.PORT)

# mapping that helps to determine how to filter by filters in db
#todo rubric_id filter looks difficult and query will go through all rows
filters_map = {
    "rubric_id": "(SELECT json_array_elements(data->'rubrics')->>'id' LIMIT 1)::int",
    "company_id": "company_id"
}


def get_branches_full(project_id, branch_ids=None, offset=None, limit=None, filters=None):
    """
    get all the branches by specified ids (full version, with attributes and rubrics)
    :param branch_ids: the list of branch ids to return
    :return:
    """

    branches = get_entity("public.branch", ids=branch_ids, filters=filters, offset=offset, limit=limit)

    # get attributes and rubrics
    attr_ids = set()
    rubric_ids = set()
    for b in branches:
        attrs = b["data"].get("attributes")
        if attrs:
            for a in attrs:
                attr_ids.add(str(a["id"]))

        rubrics = b["data"].get("rubrics")
        if rubrics:
            for r in rubrics:
                rubric_ids.add(str(r["id"]))

    def convert_to_dict(entities):
        return {e["id"]: e for e in entities}

    attributes = convert_to_dict(get_entity("public.attribute", attr_ids))
    rubrics = convert_to_dict(get_entity("public.rubric", rubric_ids))

    result = []
    for b in branches:
        new_b = b
        new_b["attributes"] = []
        new_b["rubrics"] = []

        attrs = b["data"].get("attributes")
        if attrs:
            for a in attrs:
                new_b["attributes"].append(attributes[a["id"]])

        rbrs = b["data"].get("rubrics")
        if rbrs:
            for r in rbrs:
                new_b["rubrics"].append(rubrics[r["id"]])

        result.append(new_b)

    return branches


def sql_filters(filters):
    return " AND ".join(["%s=%s" % (filters_map[k], v) for (k, v) in filters.items()])


def get_entity(name, ids=None, filters=None, offset=None, limit=None):
    """
    retrieves all the entities by specified name (table with schema) and ids
    :param name:
    :param ids:
    :param filters: a dict who's keys are column name and values are column values. will be put in where clause
    :return:
    """

    with get_cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cur:
        if not ids:
            query = "SELECT * FROM %s as e" % name
        else:
            query = "SELECT * FROM %s as e WHERE e.id in (%s)" % (name, ",".join(ids))

        if filters:
            if ids:
                query += " AND "
            else:
                query += " WHERE "

            query += sql_filters(filters)

        if limit:
            query += " LIMIT %s" % str(limit)

        if offset:
            query += " OFFSET %s" % str(offset)

        logging.info("Running query %s" % query)
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


def count(name, filters=None):
    """
    gets the count of a given entity with give filters
    :param name: the name of the entity with schema (e.g. public.branch)
    :param filters: the dictionary of filter to apply on entity in WHERE clause
    :return:
    """
    with get_cursor() as cur:
        query = "SELECT COUNT(1) FROM %s " % name
        if filters:
            query = query + "WHERE " + sql_filters(filters)
        logging.info("Running query %s" % query)
        cur.execute(query)
        return cur.fetchone()[0]
