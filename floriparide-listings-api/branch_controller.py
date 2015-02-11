import json
import random
import bottle
import branch_service
from util.controller_utils import validate, json_response, enable_cors


app = bottle.Bottle()


@app.get("/list")
@json_response
@enable_cors
@validate(locale=str, project_id=int, company_id=int, rubric_id=int, start=int, limit=int)
def get_list(project_id, start, limit, locale='pt_Br', company_id=None, rubric_id=None):
    """
    Gets a set of branches by specified filters - rubric id and company id.
    :param project_id: project id to search branch in. Required
    :param start: the start index of result to return from (paging). Required
    :param limit: the size of results to return (paging). Required
    :param locale: the localization of a result set. Optional (default=pt_Br)
    :param company_id: the id of a company to. Optional (if not specified, rubric_id must be specified)
    :param rubric_id: the id of a rubric to filter out results. Optional (if not, specified company_id must be specified)
    :return:
    """
    # make some validation before
    if not company_id and not rubric_id:
        raise bottle.HTTPError(status=400,
                               body='Either rubric_id or company_id should be specified in request')

    # branch will be a list of size 1 if the item was found
    branches, total = branch_service.get_list(project_id, company_id=company_id, rubric_id=rubric_id, start=start,
                                              limit=limit)

    if not branches:
        raise bottle.HTTPError(status=404,
                               body='No branches were found for given request')

    result = {
        'total': total
    }
    if start == 0:
        result['markers'] = branch_service.get_markers(branches)

    if limit:
        if limit > total:
            limit = total
        branches = branches[:limit]

    result['items'] = branch_response(branches, locale)

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
@validate(q=str, project_id=int, start=int, limit=int, locale=str, attrs=str)
def search(q, project_id, start, limit, locale='pt_Br', attrs=None):
    # todo get index name from db by project id
    # todo get default locale by project id

    result = {}
    branches, total = branch_service.search(q, project_id, start, limit, attrs)

    def localize_names(obj):
        """
        obj should contain key "names"
        """
        if obj:
            obj['name'] = obj['names'].get(locale)
            obj.pop('names', None)
            return obj

    # prepare markers with branch_id, name, lat, lon
    if start == 0:
        result['markers'] = branch_service.get_markers(branches)
        result['top_rubrics'], top_attributes = branch_service.get_top_rubrics(branches)
        for a in top_attributes:
            a['attributes'] = list(map(localize_names, a['attributes']))
        result['top_attributes'] = list(map(localize_names, top_attributes))
    # cut the resulting list. Only after we get markers and top rubrics!!!
    # Cuz markers and top rubrics are calculated based on full search result
    if limit:
        if limit > total:
            limit = total
        branches = branches[:limit]

    result['items'] = branch_response(branches, locale)
    result['total'] = total

    return result


def branch_response(branches, locale):
    """
    prepares branches to be sent to client
    :param branches:
    :return:
    """
    if not branches:
        return []

    # localize attributes and rubrics by specified locale
    def localize(not_localized):
        if not_localized:
            return [dict(id=a["id"], name=a["data"]["names"].get(locale)) for a in not_localized]

    def payment_opts(raw_opts):
        if raw_opts:
            opts = []
            for k, v in raw_opts.items():
                if v:
                    opts += v
            opts.sort()
            return opts

    def company(raw_company):
        if raw_company:
            return dict(id=raw_company['id'], name=raw_company['name'],
                        branch_count=1)
            # branch_count=raw_company.get('data', {}).get('branch_count', 0))

    return [dict(id=b["id"],
                 name=b["name"],
                 attributes=localize(b["draft"].get("attributes")),
                 rubrics=localize(b["draft"].get("rubrics")),
                 address=b["draft"].get("address"),
                 contacts=b["draft"].get("contacts"),
                 payment_options=payment_opts(b["draft"].get("payment_options")),
                 schedule=b["draft"].get("schedule"),
                 description=b["draft"].get("description"),
                 article=b["draft"].get("article"),
                 photos=b["draft"].get("photos"),
                 company=company(b["draft"].get("company")),
                 comments=hardcoded_comments(),
                 rating=round(random.uniform(0, 5), 1),
                 geometry=b["draft"].get("geometry"))
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
