import glob
import os
import re
import urllib.request
import time
import shutil
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
city_path = 'florianopolis/'
city_host = host + city_path

def check_crete_folder(folder):
    if not os.path.exists(folder):
        os.mkdir(folder)


def access_resource(request):
    attempt = 0
    while True:
        try:
            attempt += 1
            return urllib.request.urlopen(request)
        except urllib.error.HTTPError as e:
            print('Oops!  Awful server response.')
            if attempt > 10:
                print('Giving up')
                raise e
            time.sleep(30)
            print('Trying again...')


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


def get_url_name(name):
    return urllib.request.quote(re.sub('\s+', '-', re.sub('\s+-\s+', ' ', name.strip(' '))).replace('/', '').replace(',',''))


def get_branch_url_name(branch):
    name = branch['name'] + ' '
    name += get_street_address(branch)
    return get_url_name(name)


def branch_snapshot():
    """
    creates a branch pages snapshot
    :return:
    """
    #todo dynamically resolve city path
    root_branch_url = home_url + city_path + 'firm/'
    root_path = home_path + city_path + 'firm/'
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

        resp = access_resource(req)

        html = resp.read()

        f_dir = root_path + str(b['id']) + '/'
        if not os.path.exists(f_dir):
            os.mkdir(f_dir)
        else:
            file_list = glob.glob(f_dir + '*')
            for f in file_list:
                os.remove(f)
        with open(f_dir + str(b['id']), 'wb') as f:
            html = re.sub(b'<title>.*</title>', bytes(get_title(b), encoding='utf8'), html)
            html = html.replace(bytes('#!/', encoding='utf8'), bytes(city_host, encoding='utf8'))
            html = html.replace(bytes('<meta name="description" content="">', encoding='utf8'),
                                bytes(get_description(b),
                                      encoding='utf8'))
            html = re.sub(b'<div class="description-text ng-binding" ng-class="\{show: !collapseDescr}">.+<\/div>',
                          bytes('', encoding='utf8'), html)
            f.write(html)

        print('Done %d' % b['id'])
        time.sleep(5)


# prepare rubric list page


def get_rubric_url_name(rubric):
    name = rubric['data']['names']['pt_Br']

    return get_url_name(name)


def get_rubric_title(rubric):
    return '%s %s' % (rubric['data']['names']['pt_Br'], 'em Florianópolis')


def get_rubric_description(rubric):
    return '%s%s' % (get_rubric_title(rubric), ': todas as informações sobre empresas locais')


def rubric_snapshot():
    root_rubric_url = home_url + city_path + 'rubric/'
    parent_rubric_url = home_url + city_path + 'rubrics/'
    rubric_path = home_path + city_path + 'rubric/'
    parent_rubric_path = home_path + city_path + 'rubrics/'

    check_crete_folder(rubric_path)
    check_crete_folder(parent_rubric_path)

    for k, v in rubric_map.items():
        if v['parent_id']:
            url = '%s%d/' % (root_rubric_url, k)
            f_dir = rubric_path + str(k) + '/'
        else:
            url = '%s%d/' % (parent_rubric_url, k)
            f_dir = parent_rubric_path + str(k) + '/'

        resp = access_resource(urllib.request.Request(url))
        html = resp.read()

        if not os.path.exists(f_dir):
            os.mkdir(f_dir)
        else:
            file_list = glob.glob(f_dir + '*')
            for f in file_list:
                os.remove(f)
        with open(f_dir + str(v['id']), 'wb') as f:
            title = '<title>%s</title>' % get_rubric_title(v)
            description = '<meta name="description" content="%s">' % get_rubric_description(v)
            html = re.sub(b'<title>.*</title>', bytes(title, encoding='utf8'), html)
            html = html.replace(bytes('<meta name="description" content="">', encoding='utf8'), bytes(description,
                                                                                                      encoding='utf8'))
            f.write(html)


def home_snapshot():
    resp = access_resource(urllib.request.Request(home_url + city_path))
    html = resp.read()
    with open(home_path + city_path + 'index', 'wb') as f:
        title = '<title>%s</title>' % 'RuaDeBaixo - mapa de Florianópolis e de cidades próximas: ruas, casas e ' \
                                      'negócios na cidade.'
        description = '<meta name="description" content="%s">' % \
                      'Mapa detalhado de Florianópolis. ' \
                      'Procurar por endereço, números de telefone, opiniões, fotos, horário de atendimento. ' \
                      'Procurar empresas e rotas de carro e de transportes públicos.'
        html = re.sub(b'<title>.*</title>', bytes(title, encoding='utf8'), html)
        html = html.replace(bytes('<meta name="description" content="">', encoding='utf8'), bytes(description,
                                                                                                  encoding='utf8'))
        f.write(html)


def home_rubrics_snapshot():
    url = home_url + city_path + 'rubrics/'
    rubrics_path = home_path + city_path + 'rubrics/'

    check_crete_folder(rubrics_path)

    resp = access_resource(urllib.request.Request(url))
    html = resp.read()
    with open(rubrics_path + 'Todas-as-categorias', 'wb') as f:
        title = '<title>%s</title>' % 'Todas as empresas e organizações de Florianópolis com endereços, ' \
                                      'números de telefone e horário de atendimento - Rua De Baixo'
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

    home_el = sitemap_el_tmpl() % (city_host, iso_time, 'weekly', '1.0')
    result += home_el

    home_rubric_el = sitemap_el_tmpl() % (city_host + 'rubrics/Todas-as-categorias', iso_time, 'weekly', '1.0')
    result += home_rubric_el

    #rubric
    for k, v in rubric_map.items():
        if v['parent_id']:
            url = '%s%d/%s' % (city_host + 'rubric/', k, urllib.request.quote(v['data']['names']['pt_Br'], safe=''))
        else:
            url = '%s%d/%s' % (city_host + 'rubrics/', k, urllib.request.quote(v['data']['names']['pt_Br'], safe=''))

        rubric_el = sitemap_el_tmpl() % (url, iso_time, 'monthly', '1.0')
        result += rubric_el

    # branch
    branches = branch_dao.get_full(0, order='id')

    # prepare branches pages
    for b in branches:
        url = '%sfirm/%d/%s' % (city_host, b['id'], get_branch_url_name(b))
        branch_el = sitemap_el_tmpl() % (url, iso_time, 'weekly', '1.0')
        result += branch_el

    result += '</urlset>'

    with open(config.sitemap_path + 'sitemap.xml', 'wb') as f:
        f.write(bytes(result, encoding='utf8'))


def back_up_snapshots(folder_name):
    """
    makes a backup for newly generated snapshots
    :param folder_name:
    :return:
    """
    if not os.path.exists(config.snapshot_bkp_path):
        os.makedirs(config.snapshot_bkp_path)

    try:
        shutil.copytree(config.snapshot_path, config.snapshot_bkp_path + folder_name)
    except OSError as e:
        print('Directory not copied. Error: %s' % e)


check_crete_folder(config.snapshot_path)
check_crete_folder(home_path + city_path)

branch_snapshot()
rubric_snapshot()
home_snapshot()
home_rubrics_snapshot()
sitemap()
audit_dao.update_snapshot_timestamp(new_timestamp)
back_up_snapshots(snapshot_timestamp.strftime("%Y%m%d%H%M%S"))
