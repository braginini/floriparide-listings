from mapper import audit_dao
from mapper import base_dao
import json
from mapper import branch_dao
from elasticsearch import Elasticsearch
from elasticsearch import helpers

__author__ = 'Mike'
# load timestamp from db of file
# get new_timestamp in ms from DB "SELECT EXTRACT (EPOCH FROM now())"
#do a select to audit table filtering out all entries with timestamp < timestamp
#order entries for each source_id by timestamp and take the last one
#convert to ES index JSON
#send to ES in a batch
#update timestamp in DB or file by new_timestamp


old_timestamp, new_timestamp = audit_dao.load_timestamps()
history = audit_dao.get_history(old_timestamp, "audit.a_branch")

#map with key = entity_id (e.g. branch, company) and all the rest as a value
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
attribute_history_set = set(a["data"]["id"] for a in audit_dao.get_history(old_timestamp, "audit.a_attribute")
                            if a["operation_type"] is 'U')
rubric_history_set = set(r["data"]["id"] for r in audit_dao.get_history(old_timestamp, "audit.a_rubric")
                         if r["operation_type"] is 'U')

#get the set of current rubrics and attributes
rubrics = {str(r[0]): r for r in base_dao.get_all("public.rubric")}
attributes = {str(r[0]): r for r in base_dao.get_all("public.attribute")}

other_branches = branch_dao.get_by_attrs_rubrics(attribute_history_set, rubric_history_set, branch_history_map.keys())

es_actions = []
for k, v in branch_history_map.items():
    action = {}
    operation = v["operation_type"]
    if operation is "D":
        action = {
            '_op_type': 'delete',
            '_index': 'florianopolis',
            '_type': 'branch',
            '_id': int(k)
        }
    else:
        #bulding up document for index
        #first take all fields from data
        data = {key: value for key, value in v["data"]["draft"].items() if key == "address" or key == "description"}
        #add name
        data["name"] = v["data"]["name"]
        data["company_id"] = v["data"]["company_id"]

        #add payment options
        payment_options = v["data"]["draft"].get("payment_options")
        if payment_options:
            es_p_opts = []
            if payment_options.get("credit_cards"):
                es_p_opts += payment_options.get("credit_cards")
            if payment_options.get("other"):
                es_p_opts += payment_options.get("other")
            if payment_options.get("food_cards"):
                es_p_opts += payment_options.get("food_cards")
            if payment_options.get("debit_cards"):
                es_p_opts += payment_options.get("debit_cards")

            data["payment_options"] = es_p_opts

        #add rubrics and attributes names
        data["rubrics"] = [{"names": rubrics[str(k["id"])][2]["names"], "id": str(k["id"])}
                           for k in v["data"]["draft"].get("rubrics")]
        #add location lat, lng
        if v["data"]["draft"].get("geometry"):
            point = v["data"]["draft"]["geometry"].get("point")
            #we use lat lon to support ES geo point type
            data["point"] = dict(lat=point['lat'], lon=point['lng'])

        curr_attributes = v["data"]["draft"].get("attributes")
        if curr_attributes:
            curr_attributes = [
                {"names": attributes[str(key["id"])][1]["names"], "id": str(key["id"]), "value": key["value"]}
                for key in v["data"]["draft"].get("attributes")]
        data["attributes"] = curr_attributes
        action = {
            '_op_type': 'index',
            '_index': 'florianopolis',
            '_type': 'branch',
            '_id': int(k),
            '_source': data
        }

    es_actions.append(action)

#add branches who's attributes or rubrics were updated
if other_branches:
    for b in other_branches:
        data = {key: value for key, value in b["draft"].items() if key == "address" or key == "payment_options"
                or key == "description"}
        data["name"] = b["name"]
        data["company_id"] = b["company_id"]

        #add location lat, lng
        if b["draft"].get("geometry"):
            point = b["draft"]["geometry"].get("point")
            #we use lat lon to support ES geo point type
            data["point"] = dict(lat=point['lat'], lon=point['lng'])

        #add payment options
        payment_options = b["draft"].get("payment_options")
        if payment_options:
            es_p_opts = []
            if payment_options.get("credit_cards"):
                es_p_opts += payment_options.get("credit_cards")
            if payment_options.get("other"):
                es_p_opts += payment_options.get("other")
            if payment_options.get("food_cards"):
                es_p_opts += payment_options.get("food_cards")
            if payment_options.get("debit_cards"):
                es_p_opts += payment_options.get("debit_cards")

            data["payment_options"] = es_p_opts

        data["rubrics"] = [{"names": rubrics[key["id"]][2]["names"], "id": key["id"]}
                           for key in b["data"].get("rubrics")]

        curr_attributes = b["draft"].get("attributes")
        if curr_attributes:
            curr_attributes = [{"names": attributes[key["id"]][1]["names"], "id": key["id"], "value": key["value"]}
                               for key in b["draft"].get("attributes")]
        data["attributes"] = curr_attributes
        action = {
            '_op_type': 'index',
            '_index': 'florianopolis',
            '_type': 'branch',
            '_id': b["id"],
            '_source': data
        }

        es_actions.append(action)


#update ES index and DB timestamp
if es_actions:
    es = Elasticsearch(hosts=['localhost:9200'])
    print(helpers.bulk(es, es_actions))
    print(new_timestamp)
    audit_dao.update_timestamp(new_timestamp)










