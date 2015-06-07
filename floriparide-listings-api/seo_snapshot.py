import glob
import os
import urllib.request
import dao


branch_dao = dao.branch_dao
rubric_dao = dao.rubric_dao

rubric_map = {e['id']: e for e in rubric_dao.get_list(0)}

def get_street_address(branch):
    addr = ''
    if branch['draft'].get('address') and branch['draft'].get('address').get('street'):
        addr += branch['draft'].get('address').get('street')
        if branch['draft'].get('address').get('street_number'):
            addr += ', ' + branch['draft'].get('address').get('street_number')

    return addr


def get_title(branch):
    #Name - Rubric - City
    # later instead of rubric take card_description when it will be added to branch
    title = '<title>%s' % branch['name']

    if branch['draft'].get('rubrics'):
        rubric = rubric_map[branch['draft'].get('rubrics')[0]['id']]
        title += ' - ' + rubric['data']['names']['pt_Br']
    if branch['draft'].get('address') and branch['draft'].get('address').get('city'):
        title += ' - ' + branch['draft'].get('address').get('city')

    addr = get_street_address(branch)
    if addr:
        title += '(' + addr + ')'

    title = title.strip(' ')
    return title + '</title>'


def get_description(branch):
    description = branch['name']
    addr = get_street_address(branch)
    if addr:
        description += ' - ' + addr

    description += ': ' + 'endereço, telefones, horário de atendimento e como chegar'

    description = '<meta name="description" content="%s">' % description

    return description


def get_url_name(branch):
    name = branch['name'] + ' '
    name += get_street_address(branch)

    return name.strip(' ').replace('/\s+-\s+/', '').replace('\"', '').replace('/\s/g', '-').replace(' ', '-').replace(',', '')


root_url = 'http://localhost:8888/?_escaped_fragment_=/firm/'
root_path = 'C:/Users/mikhail/Documents/projects/floriparide-listings/floriparide-listings-api/catalog/firm/'



branches = branch_dao.get_full(0, order='id')

for b in branches:
    url = '%s%d/' % (root_url, b['id'])

    resp = urllib.request.urlopen(url)

    html = resp.read()

    f_dir = root_path + str(b['id']) + '/'
    if not os.path.exists(f_dir):
        os.mkdir(f_dir)
    else:
        file_list = glob.glob(f_dir + '*')
        for f in file_list:
            os.remove(f)
    with open(f_dir + get_url_name(b) + '.html', 'wb') as f:
        html = html.replace(bytes('<title>Floripa Ride</title>', encoding='utf8'), bytes(get_title(b), encoding='utf8'))
        html = html.replace(bytes('<meta name="description" content="">', encoding='utf8'), bytes(get_description(b),
                                                                                                  encoding='utf8'))
        f.write(html)

    print('Done %d' % b['id'])


