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
    con.autocommit = True
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

    def get_entity(self, ids=None, filters=None, offset=None, limit=None, order=None):
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
                query = "SELECT * FROM %s as e WHERE e.id in (%s)" % (self.table_name, ",".join(map(str, ids)))

            if filters:
                if ids:
                    query += " AND "
                else:
                    query += " WHERE "

                query += sql_filters(filters, self.filters_map)

            if order:
                query += " ORDER BY %s" % str(order)

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
        BaseDao.__init__(self, 'public.attribute')

    def get_attributes(self, group_id):
        with get_cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cur:
            query = 'SELECT id, group_id, ' \
                    'cast(data->>(\'names\') as json) as names,  ' \
                    'data->>(\'input_type\') as input_type, ' \
                    'data->>(\'filter_type\') as filter_type ' \
                    'FROM public.attribute WHERE group_id = %s'
            cur.execute(query, (group_id,))
            return cur.fetchall()


class RubricDao(BaseDao):
    def __init__(self):
        BaseDao.__init__(self, 'public.rubric')

    def get_attribute_groups(self, rubric_ids):
        with get_cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cur:
            str_ids = [str(r) for r in rubric_ids]
            query = 'SELECT id, ' \
                    'cast(data->>(\'names\') as json) as names, ' \
                    'data->>(\'description\') as description ' \
                    'FROM public.attributes_group ' \
                    'WHERE id IN (SELECT attributes_group_id FROM public.rubric_attributes_group ' \
                    'WHERE rubric_id in %s)'

            cur.execute(query, (tuple(rubric_ids),))
            return cur.fetchall()

    def get_attributes(self, rubric_ids):
        '''
        returns a list of attributes along with attribute_group data for a given rubric.
        :param rubric_ids:
        :return:
        '''
        with get_cursor() as cur:
            query = 'SELECT a.id attribute_id, ag.id group_id, a.data attribute_data, ag.data group_data ' \
                    'FROM public.attribute a ' \
                    'JOIN public.attributes_group ag ON a.group_id = ag.id ' \
                    'WHERE a.group_id ' \
                    'IN (SELECT attributes_group_id FROM public.rubric_attributes_group WHERE rubric_id IN (%d));'
            cur.execute(query, (rubric_ids,))
            return cur.fetchall()


class BranchDao(BaseDao):
    def __init__(self, attribute_dao, rubric_dao, company_dao):
        # mapping that helps to determine how to filter by filters in db
        # todo rubric_id filter looks difficult and query will go through all rows
        BaseDao.__init__(self, 'public.branch',
                         dict(rubric_id='(SELECT json_array_elements(draft->\'rubrics\')->>\'id\' LIMIT 1)::int',
                              company_id='company_id'))

        self.attribute_dao = attribute_dao
        self.rubric_dao = rubric_dao
        self.company_dao = company_dao

    def get_full(self, project_id, branch_ids=None, offset=None, limit=None, filters=None, order=None):
        """
        get all the branches by specified ids (full version, with attributes and rubrics)
        :param branch_ids: the list of branch ids to return
        :return:
        """

        branches = self.get_entity(ids=branch_ids, filters=filters, offset=offset, limit=limit, order=order)

        # get attributes and rubrics ids
        attr_ids = {r['id'] for b in branches if b['draft'].get('attributes') for r in b['draft']['attributes']}
        rubric_ids = {r['id'] for b in branches if b['draft'].get('rubrics') for r in b['draft']['rubrics']}
        company_ids = {b['company_id'] for b in branches}

        def convert_to_dict(entities):
            return {e['id']: e for e in entities}

        attributes = convert_to_dict(self.attribute_dao.get_entity(attr_ids))
        rubrics = convert_to_dict(self.rubric_dao.get_entity(rubric_ids))
        companies = convert_to_dict(self.company_dao.get_entity(company_ids))

        def convert_branch(branch):
            if branch['draft'].get('attributes'):
                branch['draft']['attributes'] = [attributes[int(attr['id'])] for attr in branch['draft']['attributes']]

            if branch['draft'].get('rubrics'):
                branch['draft']['rubrics'] = [rubrics[int(ru['id'])] for ru in branch['draft']['rubrics']]

            branch['draft']['company'] = companies[branch['company_id']]

            return branch

        return [convert_branch(b) for b in branches]

    def update(self, entity_id, field, value, is_json=False):
        with get_cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cur:
            query = "UPDATE %s SET %s = '%s'" % (self.table_name, field, value)
            if is_json:
                query += '::json'
            query += ' WHERE id=%s' % entity_id
            cur.execute(query)


class ProjectDao(BaseDao):
    def __init__(self):
        BaseDao.__init__(self, "public.project")


class CompanyDao(BaseDao):
    def __init__(self):
        BaseDao.__init__(self, "public.company")

# singletons
attribute_dao = AttributeDao()
rubric_dao = RubricDao()
company_dao = CompanyDao()
branch_dao = BranchDao(attribute_dao, rubric_dao, company_dao)
project_dao = ProjectDao()


def sql_filters(filters, filters_map):
    """
    builds a query with filters
    :param filters:
    :param filters_map:
    :return:
    """
    return " AND ".join(["%s=%s" % (filters_map[k], v) for (k, v) in filters.items()])

