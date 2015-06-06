import glob
import os
import urllib.request
import shutil
import dao

# todo read all branches from db



#todo read html from pahntom js

#todo write to folder with name and id


def get_url_name(branch):
    name = branch['name'] + ' '
    if 'address' in branch:
        name += branch['address']['street'] + ' '

    if 'address' in branch and 'street_number' in branch['address']:
        name += branch['address']['street_number']

    return name.strip(' ').replace('/\s+-\s+/', '').replace('\"', '').replace('/\s/g', '-')


root_url = 'http://localhost:8888/?_escaped_fragment_=/firm/'
root_path = 'C:\\Users\\mikhail\\Documents\\projects\\floriparide-listings\\floriparide-listings-api\\catalog\\firm\\'

branch_dao = dao.branch_dao

branches = branch_dao.get_full(0, order='id')

for b in branches:
    url = '%s%d/' % (root_url, b['id'])

    resp = urllib.request.urlopen(url)
    #resp.headers['content-type'] = 'text/html; charset=utf-8'

    html = resp.read()

    f_dir = root_path + str(b['id']) + '\\'
    if not os.path.exists(f_dir):
        os.mkdir(f_dir)
    else:
        file_list = glob.glob(f_dir + '*')
        for f in file_list:
            os.remove(f)
    with open(f_dir + get_url_name(b) + '.html', 'wb') as f:
        f.write(html)

    print('Done %d' % b['id'])


