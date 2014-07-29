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


def get_branches_full(project_id, branch_ids):
    """
    get all the branches by specified ids (full version, with attributes and rubrics)
    :param branch_ids: the list of branch ids to return
    :return:
    """
    branches = get_entity("public.branch", branch_ids)

    #get attributes and rubrics
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


def get_entity(name, ids):
    """
    retrieves all the entities by specified name (table with schema) and ids
    :param name:
    :param ids:
    :return:
    """
    if not ids:
        return []

    with get_cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cur:
        query = "SELECT * FROM %s as e WHERE e.id in (%s)" % (name, ",".join(ids))
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
