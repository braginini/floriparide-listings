__author__ = 'mikhail'
import json
import re

def convert_raw_branch(raw_branch, mapping, rubrics_map, attrs_map):
    """
    converts raw branch to one to be sent to admin api
    :param raw_branch: raw branch object as a dictionary
    :param mapping: mapping for raw data source
    :param rubrics_map: mapping for rubrics
    :param attrs_map: mapping for attributes
    :return: returns a json object for a rubric to be ready to be sent to admin api
    """

    if raw_branch.get("source") == "hagah":
        return hagah_raw_branch(raw_branch.get("data"), mapping, rubrics_map, attrs_map)



def hagah_raw_branch(data, mapping, rubrics_map, attrs_map):
    """
    hagah convertor
    :param data:
    :param mapping:
    :param rubrics_map:
    :param attrs_map:
    :return:
    """

    print(json.dumps(data["categories"]))
    result = {}
    if "facilities" in data:
        #We use regexp here to filter out unnecessary characters in facilities names e.g. Tele-entrega(blablabla).
        #In this case regexp will return just Tele-enterga
        attributes = map(lambda v: dict(id=attrs_map.get(re.sub('(\(.*\))', '', v)), value=True), data["facilities"])
        #filter out None
        attributes = {a["id"]: a for a in attributes if a["id"]}
        result[mapping["facilities"]] = list(attributes.values())

    if "categories" in data:
        rubrics = map(lambda v: dict(id=rubrics_map.get(v)), data["categories"])
        rubrics = {a["id"]: a for a in rubrics if a["id"]}
        result[mapping["categories"]] = list(rubrics.values())

    #todo other fields that need mapping in simple iteration over map fields
    if "name" in data:
        result[mapping["name"]] = data["name"]

    if "address" in data:
        result[mapping["address"]] = data["address"]

    if "contacts" in data:
        result[mapping["contacts"]] = data["contacts"]

    if "payment_options" in data:
        result[mapping["contacts"]] = data["contacts"]

    print(result)




