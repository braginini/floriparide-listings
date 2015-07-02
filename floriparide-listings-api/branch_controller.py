import json
import random
import bottle
import branch_service
import config
import itertools
from util.controller_utils import validate, json_response, enable_cors


app = bottle.Bottle()


def localize_names(obj, locale):
    """
    obj should contain key "names"
    """
    if obj:
        if not obj.get('names'):
            return
        obj['name'] = obj['names'].get(locale)
        obj.pop('names', None)
        return obj


@app.get("/list")
@json_response
@enable_cors
@validate(locale=str, project_id=int, company_id=int, rubric_id=int, start=int, limit=int, filters=str, send_attrs=bool)
def get_list(project_id, start, limit, locale='pt_Br', company_id=None, rubric_id=None, filters=None, send_attrs=False):
    """
    Gets a set of branches by specified filters - rubric id and company id.
    :param project_id: project id to search branch in. Required
    :param start: the start index of result to return from (paging). Required
    :param limit: the size of results to return (paging). Required
    :param locale: the localization of a result set. Optional (default=pt_Br)
    :param company_id: the id of a company to. Optional (if not specified, rubric_id must be specified)
    :param rubric_id: the id of a rubric to filter out results. Optional (if not, specified company_id must be specified)
    :param filters: the list of filters (attributes) to filter result with. Optional.
    :return:
    """

    if not limit or limit > config.result_limit:
        limit = config.result_limit

    if filters:
        filters = json.loads(filters)

    # make some validation before
    if not company_id and not rubric_id:
        raise bottle.HTTPError(status=400,
                               body='Either rubric_id or company_id should be specified in request')

    # branch will be a list of size 1 if the item was found
    branches, total = branch_service.get_list(project_id, company_id=company_id, rubric_id=rubric_id, start=start,
                                              limit=limit, filters=filters)

    if not branches:
        return {'total': 0, 'items': []}

    result = {
        'total': total
    }
    if start == 0:
        result['markers'] = markers_response(branch_service.get_markers(branches), locale)

    if limit:
        if limit > total:
            limit = total
        branches = branches[:limit]

    result['items'] = branch_response(branches, locale)

    if send_attrs:
        top_rubrics, top_attributes_group = branch_service.get_top_rubrics(branches)
        for a in top_attributes_group:
            a['attributes'] = list(map(lambda attr: localize_names(attr, locale), a['attributes']))
            localize_names(a, locale)
        result['top_attributes'] = top_attributes_group

    return result


@app.get("/")
@json_response
@enable_cors
@validate(locale=str, project_id=int, id=str)
def get(project_id, id, locale='pt_Br'):
    """
    gets one or more branches by specified id(ids)
    :param project_id: project id to search branch in. Required
    :param id: comma-separated list of branch ids
    :param locale: locale of a result
    :return:
    """
    # branch will be a list of size 1 if the item was found
    branch_ids = id.split(",")
    branches = branch_service.get(project_id, branch_ids)
    if not branches:
        raise bottle.HTTPError(status=404,
                               body='No branches were found for given id=%s and project_id=%d' % (id, project_id))
    return {'items': branch_response(branches, locale), 'total': len(branches)}


@app.get('/search')
@json_response
@enable_cors
@validate(q=str, project_id=int, start=int, limit=int, locale=str, filters=str, send_attrs=bool)
def search(q, project_id, start, limit, locale='pt_Br', filters=None, send_attrs=False):
    # todo get index name from db by project id
    # todo get default locale by project id

    if not limit or limit > config.result_limit:
        limit = config.result_limit

    result = {}
    if filters:
        filters = json.loads(filters)

    branches, total = branch_service.search(q, project_id, start, limit, filters)

    # prepare markers with branch_id, name, lat, lon
    top_rubrics, top_attributes_group = branch_service.get_top_rubrics(branches)
    if start == 0:
        result['markers'] = markers_response(branch_service.get_markers(branches), locale)
        result['top_rubrics'] = top_rubrics

    if send_attrs:
        for a in top_attributes_group:
            a['attributes'] = list(map(lambda attr: localize_names(attr, locale), a['attributes']))
            localize_names(a, locale)
        result['top_attributes'] = top_attributes_group

    # cut the resulting list. Only after we get markers and top rubrics!!!
    # Cuz markers and top rubrics are calculated based on full search result
    if limit:
        if limit > total:
            limit = total
        branches = branches[:limit]

    result['items'] = branch_response(branches, locale)
    result['total'] = total

    return result


def markers_response(markers, locale):
    """
    prepares markers to be sent to client
    :param markers:
    :return:
    """
    if not markers:
        return []

    return [dict(branch_id=b['branch_id'],
                 name=b['name'],
                 lat=b['lat'],
                 lng=b['lng'],
                 paid=b['paid'],
                 attributes=attributes_response(b.get('attributes'), locale))
            for b in markers]


# localize attributes and rubrics by specified locale
def attributes_response(not_localized, locale):
    if not_localized:
        print(not_localized)
        return [dict(id=a['id'], name=a['data']['names'].get(locale), itemprop=a['data'].get('itemprop'),
                     input_type=a['data'].get('input_type'),
                     filter_type=a['data'].get('filter_type'),
                     value=a['data'].get('value'),
                     suffix=a['data'].get('suffix')) for a in not_localized]


def rubrics_response(not_localized, locale):
    if not_localized:
        return [dict(id=a['id'], name=a['data']['names'].get(locale), itemtype=a["data"].get('itemtype'))
                for a in not_localized]


def localize_attr_groups(not_localized, locale):
    if not_localized:
        return [dict(id=a['id'], name=a['names'].get(locale), string_id=a['string_id'],
                     icon=a.get('icon'),
                     attributes=attributes_response(a['attributes'], locale)) for a in not_localized]


def branch_response(branches, locale):
    """
    prepares branches to be sent to client
    :param branches:
    :return:
    """
    if not branches:
        return []

    def payment_opts(raw_opts):
        if raw_opts:
            opts = list(filter(None, itertools.chain(*[v if isinstance(v, list) else [v]  for v in raw_opts.values()])))
            opts.sort()
            return opts
        else:
            return []

    def company(raw_company):
        if raw_company:
            return dict(id=raw_company['id'], name=raw_company['name'],
                        branch_count=1)
            # branch_count=raw_company.get('data', {}).get('branch_count', 0))

    return [dict(id=b["id"],
                 name=b["name"],
                 attribute_groups=localize_attr_groups(b["draft"].get("attribute_groups"), locale),
                 rubrics=rubrics_response(b["draft"].get("rubrics"), locale),
                 address=b["draft"].get("address"),
                 contacts=b["draft"].get("contacts"),
                 payment_options=payment_opts(b["draft"].get("payment_options")),
                 schedule=b["draft"].get("schedule"),
                 description=b["draft"].get("description"),
                 article=b["draft"].get("article"),
                 photos=b["draft"].get("photos"),
                 company=company(b["draft"].get("company")),
                 comments={},
                 rating=b['draft'].get('rating'),
                 geometry=b["draft"].get("geometry"),
                 headline=b["draft"].get("headline"))
            for b in branches]


def hardcoded_comments():
    """
    returns a hardcoded comments object
    get rid of this shit later
    :return:
    """

    return {'total': 3,
            'items': [
                {
                    'user': {
                        'id': 1,
                        'name': 'John Biscuit',
                        'photo':
                            'https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/t1.0-1/c0.3.50.50/p50x50/970996_10152319709875309_746275031_n.jpg'
                    },
                    'rating': 5.0,
                    'created': 1408977990000,
                    'text': 'O melhor lugar no mundo! Mt barato.',
                    'id': 1
                },
                {
                    'user': {
                        'id': 2.0,
                        'name': 'Angelica Rating',
                        'photo': 'https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/v/t1.0-1/p50x50/10492361_10201567545070520_9154432532799480615_n.jpg?oh=cfb33694f694d2032b37e432c86ad1fb&oe=545D1296&__gda__=1417326448_3959e97f905e7d4590d39026277d4114'
                    },
                    'rating': 4.0,
                    'created': 1393857990000,
                    'text': 'Mt bom! Vou voltar mais',
                    'id': 2
                },
                {
                    'user': {
                        'id': 3.0,
                        'name': 'Bad guy',
                        'photo': 'https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/v/t1.0-1/p50x50/10492361_10201567545070520_9154432532799480615_n.jpg?oh=cfb33694f694d2032b37e432c86ad1fb&oe=545D1296&__gda__=1417326448_3959e97f905e7d4590d39026277d4114'
                    },
                    'rating': 1.0,
                    'created': 1407077190000,
                    'text': 'Que isso porra!? Nao quero voltar mais por que esse lygar e foda!',
                    'id': 3
                }
            ]}
