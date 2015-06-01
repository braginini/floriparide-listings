import json
from mapper import mappings_reader

__author__ = 'mikhail'

attr_map = mappings_reader.get_map(
    r'C:\Users\mikhail\Documents\projects\floriparide-listings\listings-etl\data\final_lists\mappings\attribute_hagah_mapping.md')

rubric_map = mappings_reader.get_map(
    r'C:\Users\mikhail\Documents\projects\floriparide-listings\listings-etl\data\final_lists\mappings\rubric_hagah_mapping.md')

days_map = {0: 'sunday', 1: 'monday', 2: 'tuesday', 3: 'wednesday', 4: 'thursday', 5: 'friday', 6: 'saturday'}

with open('C:\\Users\\mikhail\\Documents\\projects\\floriparide-listings\\listings-etl\\data\\hagah\\floripa_page_1',
          encoding='utf-8') \
        as data_file:
    data = json.load(data_file)['docs']
    for d in data:
        new = {}
        if 'categories' in d:
            cat = []
            for c in d['categories']:
                if c['name'].lower() in rubric_map:
                    cat.append(dict(id=rubric_map.get(c['name'].lower())))
            new['rubrics'] = cat

        if 'headline' in d:
            new['headline'] = d['headline']
        if 'summary' in d:
            new['description'] = d['summary']
        if 'address' in d:
            address = {}
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
            for c in d['resources']:
                if c['name'] in attr_map:
                    attrs.append(dict(id=rubric_map.get(c['name']), value=True))
            new['attributes'] = attrs
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
            attrs.append(dict(id=rubric_map.get('Capacidade'), value=d['capacity']))

        contacts = []
        if 'phones' in d:
            contacts = []
            for p in d['phones']:
                contacts.append(dict(contact='phone', value=p['value']))

        if 'sites' in d:
            for s in d['sites']:
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
            schedule = {}
            for oh in d['hours_of_operation']:
                if not 'days' in oh or not 'hours' in oh:
                    continue
                for day in range(oh['days']['from'], oh['days']['to']):
                    day_schedule = {}
                    for hour in oh['hours']:
                        m, s = divmod(hour['from'], 60)
                        h, m = divmod(m, 60)
                        frm = '%d:%02d' % (h, m)

                        m, s = divmod(hour['to'], 60)
                        h, m = divmod(m, 60)
                        to = '%d:%02d' % (h, m)

                        day_schedule['from'] = frm
                        day_schedule['to'] = to

                    schedule[days_map[d]] = day_schedule

            new['schedule'] = schedule



                # data = [json.dumps(d, ensure_ascii=False) for d in data['docs']]
                # raw_data_dao.insert(data, 'hagah')