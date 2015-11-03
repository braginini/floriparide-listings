import datetime
import json
from elasticsearch import Elasticsearch
from elasticsearch import helpers
import config
import dao

__author__ = 'Mike'
# load timestamp from db of file
# get new_timestamp in ms from DB "SELECT EXTRACT (EPOCH FROM now())"
# do a select to audit table filtering out all entries with timestamp < timestamp
# order entries for each source_id by timestamp and take the last one
# convert to ES index JSON
# send to ES in a batch
# update timestamp in DB or file by new_timestamp

es = Elasticsearch(hosts=['%s:%s' % (config.ES.HOST, config.ES.PORT)])
rubric_dao = dao.rubric_dao
attribute_dao = dao.attribute_dao
branch_dao = dao.branch_dao
audit_dao = dao.audit_dao


def recreate_index(es, index_name, doc_type):
    delete_index(es, index_name)
    create_index(es, index_name, doc_type)


def delete_index(es, index_name):
    es.indices.delete(index=index_name)


def create_index(es, index_name, doc_type):
    es.indices.create(index=index_name, body=json.loads(
        '{"settings":{"analysis":{"analyzer":{"index_analyzer":{"tokenizer":"standard","filter":["standard","lowercase","stop","asciifolding","porter_stem"]},"search_analyzer":{"tokenizer":"standard","filter":["standard","lowercase","stop","asciifolding","porter_stem"]}}},"index":{"number_of_shards":1,"number_of_replicas":1}}}'))
    es.indices.put_mapping(index=index_name, doc_type=doc_type,
                           body='{"branch":{"_all":{"enabled":true,"index_analyzer":"index_analyzer","search_analyzer":"search_analyzer"},"properties":{"id":{"type":"string","index":"not_analyzed"},"point":{"type":"geo_point"},"name":{"type":"string","boost":7,"index":"analyzed","index_analyzer":"index_analyzer","search_analyzer":"search_analyzer","store":"yes"}}}}')


old_timestamp, new_timestamp, snapshot_ts = audit_dao.load_timestamps()
history = audit_dao.get_history(old_timestamp, 'audit.a_branch')

days_map = dict(sunday=0, monday=1, tuesday=2, wednesday=3, thursday=4, friday=5, saturday=6)

# map with key = entity_id (e.g. branch, company) and all the rest as a value
branch_history_map = {}
for h in history:
    key = h["data"]["id"]
    value = branch_history_map.get(key)
    if not value:
        branch_history_map[key] = h
    else:
        #take into account only latest changes for specific id
        if h["timestamp"] > value["timestamp"]:
            branch_history_map[key] = h

#we have to track attributes and rubrics changes in order to update branches who's attribute names were changed
#we need just id
attribute_history_set = set(
    a["data"]["id"] for a in audit_dao.get_history(old_timestamp, audit_dao.table_audit_attribute)
    if a["operation_type"] is 'U')
rubric_history_set = set(r["data"]["id"] for r in audit_dao.get_history(old_timestamp, audit_dao.table_audit_rubric)
                         if r["operation_type"] is 'U')

#get the set of current rubrics and attributes
rubrics = {str(r['id']): r for r in rubric_dao.get_list(0)}
attributes = {str(a['id']): a for a in attribute_dao.get_list(0)}

other_branches = branch_dao.get_by_attrs_rubrics(attribute_history_set, rubric_history_set, branch_history_map.keys())


def format_schedule(schedule):
    if schedule:
        es_hours = {}
        for day, ranges in schedule.items():
            int_day = days_map.get(day)
            if int_day is not None:
                day_hours = []
                for r in ranges:
                    try:
                        f_date = datetime.datetime.strptime(r['from'], '%H:%M')
                        if r['to'].startswith('24:'):
                            r['to'] = r['to'].replace('24:', '00:')

                        t_date = datetime.datetime.strptime(r['to'], '%H:%M')
                        f = f_date.hour + f_date.minute / 60

                        if 0 <= t_date.hour <= 6:
                            t = 24 + t_date.hour + t_date.minute / 60
                        else:
                            t = t_date.hour + t_date.minute / 60

                        day_hours.append({'from': f, 'to': t})
                    except ValueError:
                        print(v)
                        continue
                es_hours[int_day] = day_hours

        return es_hours


def format_payment_options(payment_opts):
    if payment_opts:
        formatted = []
        if payment_opts.get("credit_cards"):
            formatted += payment_opts.get("credit_cards")
        if payment_opts.get("other"):
            formatted += payment_opts.get("other")
        if payment_opts.get("food_cards"):
            formatted += payment_opts.get("food_cards")
        if payment_opts.get("debit_cards"):
            formatted += payment_opts.get("debit_cards")
        return formatted


def format_point(geometry):
    if geometry:
        pnt = geometry.get("point")
        if pnt:
            #we use lat lon to support ES geo point type
            return dict(lat=pnt['lat'], lon=pnt['lng'])


def format_rubric(branch_rubric_entry):
    rubric_id = str(branch_rubric_entry['id'])

    return dict(names=rubrics[rubric_id]['data']['names'], id=rubric_id)


def format_attribute(branch_attribute_entry):
    attr_id = str(branch_attribute_entry['id'])
    value = branch_attribute_entry['value']
    input_type = attributes[attr_id]['data']['input_type']
    if input_type == 'timerange':
        value = [value['from'], value['to']]

    return dict(names=attributes[attr_id]['data']['names'], value=value, id=attr_id)

es_actions = []
for k, v in branch_history_map.items():
    action = {}
    operation = v['operation_type']
    if operation == 'D':
        action = {
            '_op_type': 'delete',
            '_index': config.ES.INDEX,
            '_type': 'branch',
            '_id': int(k)
        }
    else:
        #bulding up document for index
        #first take all fields from data
        data = {key: value for key, value in v['data']['draft'].items() if key == 'address' or key == 'description'}
        #add name
        data['name'] = v['data']['name']
        data['company_id'] = v['data']['company_id']
        data['tags'] = v['data']['draft'].get('tags')
        data['headline'] = v['data']['draft'].get('headline')
        if v['data']['draft'].get('rating'):
            data['rating'] = float(v['data']['draft'].get('rating'))
        else:
            data['rating'] = float(0)

        #add working hours
        data['schedule'] = format_schedule(v['data']['draft'].get('schedule'))

        #add payment options
        data['payment_options'] = format_payment_options(v['data']['draft'].get('payment_options'))

        #add rubrics and attributes names
        data['rubrics'] = [format_rubric(k) for k in v['data']['draft'].get('rubrics')]

        #add location lat, lng
        data['point'] = format_point(v['data']['draft'].get('geometry'))

        curr_attributes = v['data']['draft'].get('attributes')
        if curr_attributes:
            curr_attributes = [format_attribute(key) for key in v['data']['draft'].get('attributes')]
        data['attributes'] = curr_attributes

        action = {
            '_op_type': 'index',
            '_index': config.ES.INDEX,
            '_type': 'branch',
            '_id': int(k),
            '_source': data
        }

    es_actions.append(action)

#add branches who's attributes or rubrics were updated
if other_branches:
    for b in other_branches:
        data = {key: value for key, value in b['draft'].items() if key == 'address' or key == 'payment_options'
                or key == 'description'}
        data['name'] = b['name']
        data['company_id'] = b['company_id']
        data['tags'] = v['data']['draft'].get('tags')
        data['headline'] = v['data']['draft'].get('headline')
        if v['data']['draft'].get('rating'):
            data['rating'] = float(v['data']['draft'].get('rating'))
        else:
            data['rating'] = float(0)

        #add location lat, lng
        data['point'] = format_point(b['draft'].get('geometry'))

        #add schedule
        data['schedule'] = format_schedule(b['draft'].get('schedule'))

        #add payment options
        data['payment_options'] = format_payment_options(b['draft'].get('payment_options'))

        data['rubrics'] = [format_rubric(key) for key in b['data'].get('rubrics')]

        curr_attributes = b['draft'].get('attributes')
        if curr_attributes:
            curr_attributes = [format_attribute(key) for key in b['draft'].get('attributes')]
        data['attributes'] = curr_attributes
        action = {
            '_op_type': 'index',
            '_index': config.ES.INDEX,
            '_type': 'branch',
            '_id': b['id'],
            '_source': data
        }

        es_actions.append(action)


#update ES index and DB timestamp
if es_actions:
    print(helpers.bulk(es, es_actions))
    print(new_timestamp)
    audit_dao.update_timestamp(new_timestamp)











