import glob
import os
import urllib.request
import dao


branch_dao = dao.branch_dao
rubric_dao = dao.rubric_dao
attribute_dao = dao.attribute_dao

rubric_map = {e['id']: e for e in rubric_dao.get_list(0)}
attribute_map = {e['id']: e for e in attribute_dao.get_list(0)}


def get_street_address(branch):
    addr = ''
    if branch['draft'].get('address') and branch['draft'].get('address').get('street'):
        addr += branch['draft'].get('address').get('street')
        if branch['draft'].get('address').get('street_number'):
            addr += ', ' + branch['draft'].get('address').get('street_number')

    return addr


def get_title(branch):
    # Name - Rubric - City
    # later instead of rubric take card_description when it will be added to branch
    title = '<title>%s' % branch['name']

    if branch['draft'].get('rubrics'):
        rubric = rubric_map[branch['draft'].get('rubrics')[0]['id']]
        title += ' - ' + rubric['data']['names']['pt_Br']
    if branch['draft'].get('address') and branch['draft'].get('address').get('city'):
        title += ' - ' + branch['draft'].get('address').get('city')

    addr = get_street_address(branch)
    if addr:
        title += ' (' + addr + ')'

    title = title.strip(' ')
    return title + '</title>'


def get_attributes(branch):
    attributes = []
    if branch['draft'].get('attributes'):
        for attribute in branch['draft'].get('attributes'):
            attr = attribute_map[attribute['id']]
            attributes.append(attr['data']['names']['pt_Br'])

    return attributes


def get_description(branch):
    description = branch['name']
    addr = get_street_address(branch)
    if addr:
        description += ' - ' + addr

    description += ': ' + 'endereço, telefones, horário de atendimento e como chegar'
    description += '. ' + ', '.join(get_attributes(branch))

    description = '<meta name="description" content="%s">' % description

    return description


def get_branch_url_name(branch):
    name = branch['name'] + ' '
    name += get_street_address(branch)

    return name.strip(' ').replace('/\s+-\s+/', '').replace('\"', '').replace('/', '').replace('/\s/g', '-').replace(
        ' ', '-').replace(
        ',', '')


root_url = 'http://localhost:8888/?_escaped_fragment_=/firm/'
root_path = 'C:/Users/mikhail/Documents/projects/floriparide-listings/floriparide-listings-api/catalog/firm/'

branches = branch_dao.get_full(0, order='id')

#prepare branches pages
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
    with open(f_dir + get_branch_url_name(b) + '.html', 'wb') as f:
        html = html.replace(bytes('<title>Floripa Ride</title>', encoding='utf8'), bytes(get_title(b), encoding='utf8'))
        html = html.replace(bytes('<meta name="description" content="">', encoding='utf8'), bytes(get_description(b),
                                                                                                  encoding='utf8'))
        f.write(html)

    print('Done %d' % b['id'])

#prepare rubric list page


def get_rubric_url_name(rubric):
    name = rubric['data']['names']['pt_Br']

    return name.strip(' ').replace('/\s+-\s+/', '').replace('\"', '').replace(' / ', ' ').replace('/\s/g', '-').replace(
        ' ', '-').replace(
        ',', '')


def get_rubric_title(rubric):
    return '%s %s' % (rubric['data']['names']['pt_Br'], 'em Florianópolis')


def get_rubric_description(rubric):
    return '%s%s' % (get_rubric_title(rubric), ': todas as informações sobre empresas locais')


root_rubric_url = 'http://localhost:8888/?_escaped_fragment_=/rubric/'
root_rubric_path = 'C:/Users/mikhail/Documents/projects/floriparide-listings/floriparide-listings-api/catalog/rubric/'

for k, v in rubric_map.items():
    url = '%s%d/%s' % (root_rubric_url, k, urllib.request.quote(v['data']['names']['pt_Br']))
    resp = urllib.request.urlopen(url)
    html = resp.read()

    f_dir = root_rubric_path + str(k) + '/'
    if not os.path.exists(f_dir):
        os.mkdir(f_dir)
    else:
        file_list = glob.glob(f_dir + '*')
        for f in file_list:
            os.remove(f)
    with open(f_dir + get_rubric_url_name(v) + '.html', 'wb') as f:
        title = '<title>%s</title>' % get_rubric_title(v)
        description = '<meta name="description" content="%s">' % get_rubric_description(v)
        html = html.replace(bytes('<title>Floripa Ride</title>', encoding='utf8'),
                            bytes(title, encoding='utf8'))
        html = html.replace(bytes('<meta name="description" content="">', encoding='utf8'), bytes(description,
                                                                                                  encoding='utf8'))
        f.write(html)


