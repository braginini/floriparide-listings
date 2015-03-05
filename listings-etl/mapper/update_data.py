import json
from mapper import branch_dao

__author__ = 'mikhail'

branches = branch_dao.get_all()

to_update = []
for b in branches:
    if 'draft' in b:
        data = b['draft']
        attributes = data.get('attributes')
        if 'payment_options' in data:
            if not attributes:
                attributes = []
                data['attributes'] = attributes

            if data['payment_options'].get('credit_cards'):
                attributes.append(dict(id='31', value=True))
            if data['payment_options'].get('debit_cards'):
                attributes.append(dict(id='30', value=True))
            if data['payment_options'].get('food_cards'):
                attributes.append(dict(id='32', value=True))
            if data['payment_options'].get('other') and 'Aceita cheque' in data['payment_options'].get('other'):
                attributes.append(dict(id='33', value=True))

        if attributes:
            attributes = [e for e in attributes if e['id'] != '26']
            b['draft'] = data
            print(b['draft'].get('attributes'))
            to_update.append(b)

if to_update:
    for u in to_update:
        branch_dao.update_draft(u)
