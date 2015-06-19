import json
from mapper import raw_data_dao
import util

__author__ = 'mikhail'

days_map = {7: 'sunday', 1: 'monday', 2: 'tuesday', 3: 'wednesday', 4: 'thursday', 5: 'friday', 6: 'saturday'}

with open('C:\\Users\\mikhail\\Documents\\projects\\floriparide-listings\\listings-etl\\data\\hagah\\floripa_page_1',
          encoding='utf-8') \
        as data_file:
    data = json.load(data_file)['docs']
    new_data = []
    for d in data:
        new = {}
        if 'categories' in d:
            cat = []
            for c in d['categories']:
                if 'name' in c:
                    cat.append(c['name'])
            new['rubrics'] = cat

        if 'headline' in d:
            new['headline'] = d['headline']
        if 'summary' in d:
            new['description'] = util.strip_tags(d['summary'])
        if 'address' in d:
            if ''
            address = {}
            new['address'] = address
            if 'loc' in d['address']:
                point = dict(lat=d['address']['loc'][1], lng=d['address']['loc'][0])
                new['geometry'] = dict(point=point)

            address['street'] = d['address'].get('street')
            address['street_number'] = d['address'].get('number')
            address['street_type'] = d['address'].get('street_type')
            address['state'] = d['address'].get('state_name')
            address['country'] = 'Brazil'
            address['neighborhood'] = d['address'].get('neighborhood')
            address['city'] = d['address'].get('city')
            address['zip'] = d['address'].get('zip')

        if 'resources' in d:
            attrs = []
            new['attributes'] = attrs
            for c in d['resources']:
                attrs.append(dict(name=c['name'], value=True))

        if 'billing_options' in d:
            payment_options = {}
            if 'debit' in d['billing_options']:
                debit_cards = []
                for de in d['billing_options']['debit']:
                    debit_cards.append(de.get('name'))

                payment_options['debit_cards'] = debit_cards

            if 'credit' in d['billing_options']:
                credit_cards = []
                for cr in d['billing_options']['credit']:
                    credit_cards.append(cr.get('name'))

                payment_options['credit_cards'] = credit_cards

            if 'ticket' in d['billing_options']:
                food_cards = []
                for fo in d['billing_options']['ticket']:
                    food_cards.append(fo.get('name'))
                payment_options['food_cards'] = food_cards

            if 'other' in d['billing_options']:
                other = []
                for o in d['billing_options']['other']:
                    other.append(o.get('name'))
                payment_options['other'] = other

            new['payment_options'] = payment_options

        if 'name' in d:
            new['name'] = d['name']

        if 'keywords' in d:
            new['tags'] = d['keywords']

        if 'updated_at' in d:
            new['updated_at'] = d['updated_at'].get('$date')

        if 'inauguration' in d:
            new['since'] = d['inauguration']

        # capacity is an attribute with id = 46
        if 'capacity' in d:
            attrs = new.get('attributes')
            if not attrs:
                attrs = []
            attrs.append(dict(name='Capacidade', value=d['capacity']))

        contacts = []
        new['contacts'] = contacts
        if 'phones' in d:
            for p in d['phones']:
                contacts.append(dict(contact='phone', value=p['value']))

        if 'sites' in d:
            for s in d['sites']:
                if 'type' not in d['sites']:
                    continue
                if s['type'] == 'site':
                    contacts.append(dict(contact='website', value=s['value']))
                elif s['type'] == 'facebook':
                    contacts.append(dict(contact='facebook', value=s['value']))
                elif s['type'] == 'twitter':
                    contacts.append(dict(contact='twitter', value=s['value']))
                elif s['type'] == 'blog':
                    contacts.append(dict(contact='blog', value=s['value']))
                elif s['type'] == 'instagram':
                    contacts.append(dict(contact='instagram', value=s['value']))
                elif s['type'] == 'google':
                    contacts.append(dict(contact='google', value=s['value']))

        if 'svas' in d and 'email' in d['svas']:
            contacts.append(dict(contact='email', value=d['svas']['email'].get('address')))

        if 'hours_of_operation' in d:
            schedule = {'monday': [], 'tuesday': [], 'wednesday': [], 'thursday': [], 'friday': [], 'saturday': [],
                        'sunday': [], 'holidays': []}

            for oh in d['hours_of_operation']:
                if not 'days' in oh or not 'hours' in oh:
                    continue

                start_day = oh['days']['from']
                end_day = oh['days'].get('to')

                def get_hours(raw):
                    hs = []
                    for hr in raw['hours']:
                        m, s = divmod(hr['from'], 60)
                        h, m = divmod(m, 60)
                        frm = '%02d:%02d' % (h, m)

                        m, s = divmod(hr['to'], 60)
                        h, m = divmod(m, 60)
                        to = '%02d:%02d' % (h, m)

                        hs.append({'from': frm, 'to': to})

                    return hs

                if start_day is 7:
                    schedule['holidays'] += get_hours(oh)
                    continue

                if start_day is 8:
                    hrs = get_hours(oh)
                    schedule['saturday'] += hrs
                    schedule['sunday'] += hrs
                    continue

                if start_day is 9:
                    hrs = get_hours(oh)
                    schedule['saturday'] += hrs
                    schedule['sunday'] += hrs
                    schedule['holidays'] += hrs
                    continue

                if start_day is 10:
                    hrs = get_hours(oh)
                    schedule['monday'] += hrs
                    schedule['tuesday'] += hrs
                    schedule['wednesday'] += hrs
                    schedule['thursday'] += hrs
                    schedule['friday'] += hrs
                    schedule['saturday'] += hrs
                    schedule['sunday'] += hrs
                    schedule['holidays'] += hrs
                    continue

                if start_day is 0:
                    start_day = 7

                if end_day is None:
                    end_day = start_day

                if end_day is 0:
                    end_day = 7

                if start_day > 7 or end_day > 7:
                    continue

                for day in range(start_day, end_day + 1):
                    schedule[days_map[day]] += get_hours(oh)

            new['schedule'] = schedule

        if 'legacy' in d and 'photos' in d['legacy']:
            photos = []
            for p in d['legacy']['photos']:
                photos.append(p['media'])

            new['photos'] = photos

        new_data.append(dict(draft=json.dumps(new, ensure_ascii=False), raw=json.dumps(d, ensure_ascii=False)))

    raw_data_dao.insert(new_data, 'hagah')