import glob
import os
import re
import urllib.request
import time
import config
import dao

branch_dao = dao.branch_dao
rubric_dao = dao.rubric_dao
attribute_dao = dao.attribute_dao
audit_dao = dao.audit_dao
old_timestamp, new_timestamp, snapshot_timestamp = audit_dao.load_timestamps()
history = audit_dao.get_history(snapshot_timestamp, 'audit.a_branch')

#prepare only changed branches
branch_history = {h['data']['id'] for h in history}
print('%d branch snapshots to make' % len(branch_history))

rubric_map = {e['id']: e for e in rubric_dao.get_list(0)}
attribute_map = {e['id']: e for e in attribute_dao.get_list(0)}

home_url = config.snapshot_server_url
home_path = config.snapshot_path
host = config.snapshot_host


def get_street_address(branch):
    addr = ''
    if branch['draft'].get('address') and isinstance(branch['draft'].get('address'), dict) and branch['draft'].get(
            'address').get('street'):
        addr += branch['draft'].get('address').get('street')
        if branch['draft'].get('address').get('street_number'):
            addr += ', ' + branch['draft'].get('address').get('street_number')

    return addr


def get_title(branch):
    # Name - Rubric - City
    # later instead of rubric take card_description when it will be added to branch
    title = '<title>%s' % branch['name']

    if branch['draft'].get('headline'):
        title += ', ' + branch['draft'].get('headline')
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

    if branch['draft'].get('headline'):
        description += ', ' + branch['draft'].get('headline')

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

    return urllib.request.quote(re.sub('\s+', '-', re.sub('\s+-\s+', ' ', name.strip(' '))).replace('/', '').replace(',',''))


def branch_snapshot():
    """
    creates a branch pages snapshot
    :return:
    """

    root_branch_url = home_url + 'firm/'
    root_path = home_path + 'firm/'
    if not os.path.exists(root_path):
        os.mkdir(root_path)

    branches = branch_dao.get_full(0, order='id')

    # prepare branches pages
    for b in branches:
        if b['id'] not in branch_history:
            continue

        url = '%s%d/' % (root_branch_url, b['id'])
        req = urllib.request.Request(url)
        req.add_header('X-Prerender-Token', '41p3g3K5rzCAYppic2Fd')
        resp = urllib.request.urlopen(req)

        html = resp.read()

        f_dir = root_path + str(b['id']) + '/'
        if not os.path.exists(f_dir):
            os.mkdir(f_dir)
        else:
            file_list = glob.glob(f_dir + '*')
            for f in file_list:
                os.remove(f)
        with open(f_dir + get_branch_url_name(b), 'wb') as f:
            html = re.sub(b'<title>.*</title>', bytes(get_title(b), encoding='utf8'), html)
            html = html.replace(bytes('#!/', encoding='utf8'), bytes(host, encoding='utf8'))
            html = html.replace(bytes('<meta name="description" content="">', encoding='utf8'),
                                bytes(get_description(b),
                                      encoding='utf8'))
            f.write(html)

        print('Done %d' % b['id'])


# prepare rubric list page


def get_rubric_url_name(rubric):
    name = rubric['data']['names']['pt_Br']

    return urllib.request.quote(re.sub('\s+', '-', re.sub('\s+-\s+', ' ', name.strip(' '))).replace('/', ''))


def get_rubric_title(rubric):
    return '%s %s' % (rubric['data']['names']['pt_Br'], 'em Florianópolis')


def get_rubric_description(rubric):
    return '%s%s' % (get_rubric_title(rubric), ': todas as informações sobre empresas locais')


def rubric_snapshot():
    root_rubric_url = home_url + 'rubric/'
    parent_rubric_url = home_url + 'rubrics/'
    rubric_path = home_path + 'rubric/'
    parent_rubric_path = home_path + 'rubrics/'
    if not os.path.exists(rubric_path):
        os.mkdir(rubric_path)

    if not os.path.exists(parent_rubric_path):
        os.mkdir(parent_rubric_path)

    for k, v in rubric_map.items():
        if v['parent_id']:
            url = '%s%d/%s' % (root_rubric_url, k, urllib.request.quote(v['data']['names']['pt_Br']))
            f_dir = rubric_path + str(k) + '/'
        else:
            url = '%s%d/%s' % (parent_rubric_url, k, urllib.request.quote(v['data']['names']['pt_Br']))
            f_dir = parent_rubric_path + str(k) + '/'

        resp = urllib.request.urlopen(url)
        html = resp.read()

        if not os.path.exists(f_dir):
            os.mkdir(f_dir)
        else:
            file_list = glob.glob(f_dir + '*')
            for f in file_list:
                os.remove(f)
        with open(f_dir + get_rubric_url_name(v), 'wb') as f:
            title = '<title>%s</title>' % get_rubric_title(v)
            description = '<meta name="description" content="%s">' % get_rubric_description(v)
            html = re.sub(b'<title>.*</title>', bytes(title, encoding='utf8'), html)
            html = html.replace(bytes('<meta name="description" content="">', encoding='utf8'), bytes(description,
                                                                                                      encoding='utf8'))
            f.write(html)


def home_snapshot():
    resp = urllib.request.urlopen(home_url)
    html = resp.read()
    with open(home_path + 'index', 'wb') as f:
        title = '<title>%s</title>' % 'Mapa de Florianópolis e de cidades próximas: ruas, casas e ' \
                                      'negócios na cidade - Go Floripa'
        description = '<meta name="description" content="%s">' % \
                      'Mapa detalhado de Florianópolis. ' \
                      'Procurar por endereço, números de telefone, opiniões, fotos, horário de atendimento. ' \
                      'Procurar empresas e rotas de carro e de transportes públicos.'
        html = re.sub(b'<title>.*</title>', bytes(title, encoding='utf8'), html)
        html = html.replace(bytes('<meta name="description" content="">', encoding='utf8'), bytes(description,
                                                                                                  encoding='utf8'))
        f.write(html)


def home_rubrics_snapshot():
    url = home_url + 'rubrics/'
    rubrics_path = home_path + 'rubrics/'

    if not os.path.exists(rubrics_path):
        os.mkdir(rubrics_path)

    resp = urllib.request.urlopen(url)
    html = resp.read()
    with open(rubrics_path + 'Todas-as-categorias', 'wb') as f:
        title = '<title>%s</title>' % 'Todas as empresas e organizações de Florianópolis com endereços, ' \
                                      'números de telefone e horário de atendimento - Go Floripa'
        description = '<meta name="description" content="%s">' % \
                      'Na lista de empresas e organizações de Florianópolis você pode facilmente encontrar exatamente o que você precisa. ' \
                      'Todas as empresas são classificadas por tipo de atividade e são marcados no mapa.'
        html = re.sub(b'<title>.*</title>', bytes(title, encoding='utf8'), html)
        html = html.replace(bytes('<meta name="description" content="">', encoding='utf8'), bytes(description,
                                                                                                  encoding='utf8'))
        f.write(html)


def sitemap_el_tmpl():
    return '<url>' \
           ' <loc>%s</loc>' \
           ' <lastmod>%s</lastmod>' \
           ' <changefreq>%s</changefreq>' \
           ' <priority>%s</priority>' \
           '</url>'


def sitemap():
    mod_time = time.time()
    iso_time = time.strftime('%Y-%m-%dT%H:%M:%S', time.localtime(mod_time))
    iso_time = time.strftime('%Y-%m-%d', time.localtime(mod_time))

    result = '<?xml version="1.0" encoding="UTF-8"?>' \
             '<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">'

    home_el = sitemap_el_tmpl() % (host, iso_time, 'weekly', '1.0')
    result += home_el

    home_rubric_el = sitemap_el_tmpl() % (host + 'rubrics/', iso_time, 'weekly', '1.0')
    result += home_rubric_el

    #rubric
    for k, v in rubric_map.items():
        if v['parent_id']:
            url = '%s%d/%s' % (host + 'rubric/', k, urllib.request.quote(v['data']['names']['pt_Br']))
        else:
            url = '%s%d/%s' % (host + 'rubrics/', k, urllib.request.quote(v['data']['names']['pt_Br']))

        rubric_el = sitemap_el_tmpl() % (url, iso_time, 'monthly', '1.0')
        result += rubric_el

    # branch
    branches = branch_dao.get_full(0, order='id')

    # prepare branches pages
    for b in branches:
        url = '%sfirm/%d/%s' % (host, b['id'], get_branch_url_name(b))
        branch_el = sitemap_el_tmpl() % (url, iso_time, 'weekly', '1.0')
        result += branch_el

    result += '</urlset>'

    with open(home_path + 'sitemap.xml', 'wb') as f:
        f.write(bytes(result, encoding='utf8'))

branch_snapshot()
rubric_snapshot()
home_snapshot()
home_rubrics_snapshot()
sitemap()
audit_dao.update_snapshot_timestamp(new_timestamp)
