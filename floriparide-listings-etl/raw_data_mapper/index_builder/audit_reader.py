import audit_dao
import base_dao
import json
__author__ = 'Mike'
#load timestamp from db of file
#get new_timestamp in ms from DB "SELECT EXTRACT (EPOCH FROM now())"
#do a select to audit table filtering out all entries with timestamp < timestamp
#order entries for each source_id by timestamp and take the last one
#convert to ES index JSON
#send to ES in a batch
#update timestamp in DB or file by new_timestamp


old_timestamp, new_timestamp = audit_dao.load_timestamps()
history = audit_dao.get_history(old_timestamp, "audit.a_branch")

rubrics = {r[0]: r for r in base_dao.get_all("public.rubric")}
attributes = {r[0]: r for r in base_dao.get_all("public.attribute")}

#map with key = entity_id (e.g. branch, company) and all the rest as a value
history_map = {}
for h in history:
    key = h["data"]["id"]
    value = history_map.get(key)
    if not value:
        history_map[key] = h
    else:
        #take into account only latest changes for specific id
        if h["timestamp"] > value["timestamp"]:
            history_map[key] = h

to_delete = []
to_update_create = []
for k, v in history_map.items():
    if v["operation_type"] is "D":
        to_delete.append(k)
    else:
        #bulding up document for index
        #first take all fields from data
        data = {key: value for key, value in v["data"]["data"].items() if key is "description" or key
                is "payment_options" or key is "address"}
        #add name
        data["name"] = v["data"]["name"]

        #add rubrics and attributes names
        data["rubrics"] = [rubrics[k["id"]][2]["names"] for k in v["data"]["data"].get("rubrics")]
        curr_attributes = v["data"]["data"].get("attributes")
        if curr_attributes:
            curr_attributes = [attributes[k["id"]][1]["names"] for k in v["data"]["data"].get("attributes")]
        data["attributes"] = curr_attributes
        to_update_create.append(data)

print(json.dumps(to_update_create, ensure_ascii=False))
print(json.dumps(to_delete, ensure_ascii=False))










