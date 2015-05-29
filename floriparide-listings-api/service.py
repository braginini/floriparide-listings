import dao

__author__ = 'mikhail'


feedback_dao = dao.feedback_dao


def save_feedback(project_id, name, email, body, rating):
    feedback_dao.save(project_id, name, email, body, rating)
