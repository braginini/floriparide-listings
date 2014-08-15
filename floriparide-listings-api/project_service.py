import dao

__author__ = 'Mike'


project_dao = dao.project_dao


def get_list():
    """

    :return:
    """
    return project_dao.get_entity()




