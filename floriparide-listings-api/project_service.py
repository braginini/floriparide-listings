import dao

__author__ = 'Mike'


project_dao = dao.project_dao


def get(project_ids=None, start=None, limit=None):
    """
    gets a list of projects by specified ids
    :param project_ids: a set of project ids to return. If not specified returns all projects
    :return:
    """
    return project_dao.get_entity(ids=project_ids, offset=start, limit=limit)





