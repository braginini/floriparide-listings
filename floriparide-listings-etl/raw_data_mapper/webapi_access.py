import urllib.request
import json
import requests
__author__ = 'Mike'


def create_branch(branch):
    """

    :param branch:
    :return:
    """

    req = urllib.request.Request('http://localhost:8080/admin/v1/company')
    req.add_header('Content-Type', 'application/json')
    company = {k: v for k, v in branch.items() if k == "name" or k == "description"}
    company["project_id"] = 0
    response = urllib.request.urlopen(req, json.dumps(company).encode("UTF-8"))
    company_id = int(response.read())
    #print(company_id)

    branch["company_id"] = company_id
    req = urllib.request.Request('http://localhost:8080/admin/v1/branch')
    req.add_header('Content-Type', 'application/json')
    response = urllib.request.urlopen(req, json.dumps(branch).encode("UTF-8"))
    branch_id = int(response.read())
    print("%s" % str(branch_id))
    return branch_id


def delete_branch(branch_id):
    """
    deletes branch by specified id
    """
    headers = {'content-type': 'application/json'}
    r = requests.delete('http://localhost:8080/admin/v1/branch/%s' % branch_id, headers=headers)
    print(r.status_code)
    if r.status_code == 200:
        return True

    return False


def simple_create_branch(branch):
    """
    creates a branch
    """
    headers = {'content-type': 'application/json'}
    payload = json.dumps(branch).encode("UTF-8")
    r = requests.post('http://localhost:8080/admin/v1/branch', headers=headers, data=payload)
    branch_id = int(r.read())
    print(branch_id)
    return branch_id
