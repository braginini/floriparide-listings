import branch_dao
import webapi_access
__author__ = 'Mike'

branches = branch_dao.get_all_full()
for b in branches:
    if webapi_access.delete_branch(b["id"]):
        webapi_access.simple_create_branch(b)
