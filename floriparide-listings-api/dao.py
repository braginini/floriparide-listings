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


class BaseDao(object):
    """
    base class for dao
    """

    def __init__(self, table_name, filters_map=None):
        self.table_name = table_name
        self.filters_map = filters_map

    def get_entity(self, ids=None, filters=None, offset=None, limit=None):
        """
        retrieves all the entities by specified name (table with schema) and ids
        :param ids:
        :param filters: a dict who's keys are column name and values are column values. will be put in where clause
        :return:
        """

        with get_cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cur:
            if not ids:
                query = "SELECT * FROM %s as e" % self.table_name
            else:
                query = "SELECT * FROM %s as e WHERE e.id in (%s)" % (self.table_name, ",".join(ids))

            if filters:
                if ids:
                    query += " AND "
                else:
                    query += " WHERE "

                query += sql_filters(filters, self.filters_map)

            if limit:
                query += " LIMIT %s" % str(limit)

            if offset:
                query += " OFFSET %s" % str(offset)

            logging.info("Running query %s" % query)
            cur.execute(query)
            return cur.fetchall()

    def count(self, filters=None):
        """
        gets the count of a given entity with give filters
        :param filters: the dictionary of filter to apply on entity in WHERE clause
        :return:
        """
        with get_cursor() as cur:
            query = "SELECT COUNT(1) FROM %s " % self.table_name
            if filters:
                query = query + "WHERE " + sql_filters(filters, self.filters_map)
            logging.info("Running query %s" % query)
            cur.execute(query)
            return cur.fetchone()[0]


class AttributeDao(BaseDao):
    def __init__(self):
        BaseDao.__init__(self, "public.attribute")


class RubricDao(BaseDao):
    def __init__(self):
        BaseDao.__init__(self, "public.attribute")


class BranchDao(BaseDao):
    def __init__(self, attribute_dao, rubric_dao):
        # mapping that helps to determine how to filter by filters in db
        # todo rubric_id filter looks difficult and query will go through all rows
        BaseDao.__init__(self, "public.branch",
                         dict(rubric_id="(SELECT json_array_elements(data->'rubrics')->>'id' LIMIT 1)::int",
                              company_id="company_id"))

        self.attribute_dao = attribute_dao
        self.rubric_dao = rubric_dao

    def get_full(self, project_id, branch_ids=None, offset=None, limit=None, filters=None):
        """
        get all the branches by specified ids (full version, with attributes and rubrics)
        :param branch_ids: the list of branch ids to return
        :return:
        """

        branches = self.get_entity(ids=branch_ids, filters=filters, offset=offset, limit=limit)

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

        attributes = convert_to_dict(self.attribute_dao.get_entity(attr_ids))
        rubrics = convert_to_dict(self.rubric_dao.get_entity(rubric_ids))

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


class ProjectDao(BaseDao):
    def __init__(self):
        BaseDao.__init__(self, "public.project")


#singletons
attribute_dao = AttributeDao()
rubric_dao = RubricDao()
branch_dao = BranchDao(attribute_dao, rubric_dao)
project_dao = ProjectDao()


def sql_filters(filters, filters_map):
    """
    builds a query with filters
    :param filters:
    :param filters_map:
    :return:
    """
    return " AND ".join(["%s=%s" % (filters_map[k], v) for (k, v) in filters.items()])

