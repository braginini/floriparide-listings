from datetime import datetime
import glob
import os
import re
import urllib.request
import time
import dao


branch_dao = dao.branch_dao
rubric_dao = dao.rubric_dao
attribute_dao = dao.attribute_dao

rubric_map = {e['id']: e for e in rubric_dao.get_list(0)}
attribute_map = {e['id']: e for e in attribute_dao.get_list(0)}

home_url = 'http://localhost:8888/?_escaped_fragment_=/'
home_path = 'C:/Users/mikhail/Documents/projects/floriparide-listings/floriparide-listings-api/snapshots/'
host = 'http://localhost:9999/#!/'


def get_street_address(branch):
    addr = ''
    if branch['draft'].get('address') and isinstance(branch['draft'].get('address'), dict) and branch['draft'].get('address').get('street'):
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

    return re.sub('\s+', '-',  re.sub('\s+-\s+', ' ', name.strip(' '))).replace('/', '')


def branch_snapshot():
    """
    creates a branch pages snapshot
    :return:
    """

    root_branch_url = home_url + 'firm/'
    root_path = home_path + 'firm/'

    branches = branch_dao.get_full(0, order='id')

    # prepare branches pages
    for b in branches:
        url = '%s%d/' % (root_branch_url, b['id'])

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
            html = html.replace(bytes('<title>Floripa Ride</title>', encoding='utf8'),
                                bytes(get_title(b), encoding='utf8'))
            html = html.replace(bytes('<meta name="description" content="">', encoding='utf8'),
                                bytes(get_description(b),
                                      encoding='utf8'))
            f.write(html)

        print('Done %d' % b['id'])


# prepare rubric list page


def get_rubric_url_name(rubric):
    name = rubric['data']['names']['pt_Br']

    return re.sub('\s+', '-',  re.sub('\s+-\s+', ' ', name.strip(' '))).replace('/', '')


def get_rubric_title(rubric):
    return '%s %s' % (rubric['data']['names']['pt_Br'], 'em Florianópolis')


def get_rubric_description(rubric):
    return '%s%s' % (get_rubric_title(rubric), ': todas as informações sobre empresas locais')


def rubric_snapshot():
    root_rubric_url = home_url + 'rubric/'
    parent_rubric_url = home_url + 'rubrics/'
    rubric_path = home_path + 'rubric/'
    parent_rubric_path = home_path + 'rubrics/'

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
        with open(f_dir + get_rubric_url_name(v) + '.html', 'wb') as f:
            title = '<title>%s</title>' % get_rubric_title(v)
            description = '<meta name="description" content="%s">' % get_rubric_description(v)
            html = html.replace(bytes('<title>Floripa Ride</title>', encoding='utf8'),
                                bytes(title, encoding='utf8'))
            html = html.replace(bytes('<meta name="description" content="">', encoding='utf8'), bytes(description,
                                                                                                      encoding='utf8'))
            f.write(html)


def home_snapshot():
    resp = urllib.request.urlopen(home_url)
    html = resp.read()
    with open(home_path + 'Home.html', 'wb') as f:
        title = '<title>%s</title>' % 'Mapa de Florianópolis e de cidades próximas: ruas, casas e ' \
                                      'organizações na cidade - Go Floripa'
        description = '<meta name="description" content="%s">' % \
                      'Mapa detalhado de Florianópolis. ' \
                      'Procurar por endereço, números de telefone, opiniões, fotos, horário de atendimento. ' \
                      'Procurar empresas e rotas de carro e de transportes públicos.'
        html = html.replace(bytes('<title>Floripa Ride</title>', encoding='utf8'),
                            bytes(title, encoding='utf8'))
        html = html.replace(bytes('<meta name="description" content="">', encoding='utf8'), bytes(description,
                                                                                                  encoding='utf8'))
        f.write(html)


def home_rubrics_snapshot():
    url = home_url + 'rubrics/'
    rubrics_path = home_path + 'rubrics/'

    resp = urllib.request.urlopen(url)
    html = resp.read()
    with open(rubrics_path + 'Todas-as-categorias.html', 'wb') as f:
        title = '<title>%s</title>' % 'Todas as empresas e organizações de Florianópolis com endereços, ' \
                                      'números de telefone e horário de atendimento - Go Floripa'
        description = '<meta name="description" content="%s">' % \
                      'Na lista de empresas e organizações de Florianópolis você pode facilmente encontrar exatamente o que você precisa. ' \
                      'Todas as empresas são classificadas por tipo de atividade e são marcados no mapa.'
        html = html.replace(bytes('<title>Floripa Ride</title>', encoding='utf8'),
                            bytes(title, encoding='utf8'))
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

    #branch
    branches = branch_dao.get_full(0, order='id')

    # prepare branches pages
    for b in branches:
        url = '%sfirm/%d/%s' % (host, b['id'], urllib.request.quote(get_branch_url_name(b)))
        branch_el = sitemap_el_tmpl() % (url, iso_time, 'weekly', '1.0')
        result +=branch_el


    result += '</urlset>'

    with open(home_path + 'sitemap.xml', 'wb') as f:
        f.write(bytes(result, encoding='utf8'))


#branch_snapshot()
#rubric_snapshot()
#home_snapshot()
#home_rubrics_snapshot()
sitemap()


