__author__ = 'mikhail'
import json

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
    print(json.dumps(data))
    result = {}
    if "facilities" in data:
        #todo reg exp facilities names!!!!!
        attributes = map(lambda v: dict(id=attrs_map[v], value=True) if v in attrs_map else None, data["facilities"])
        attributes = {a["id"]: a for a in attributes if a}
        result[mapping["facilities"]] = list(attributes.values())

    print(result)


