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
    for k in mapping.keys():
        if k in data:
            if k == "facilities":
                # We use regexp here to filter out unnecessary characters in facilities names e.g.
                # Tele-entrega(blablabla). In this case regexp will return just Tele-enterga
                attributes = map(lambda v: dict(id=attrs_map.get(re.sub('(\(.*\))', '', v)), value=True), data[k])
                #filter out None
                attributes = {a["id"]: a for a in attributes if a["id"]}
                result[mapping[k]] = list(attributes.values())
            elif k == "categories":
                rubrics = map(lambda v: dict(id=rubrics_map.get(v)), data[k])
                rubrics = {a["id"]: a for a in rubrics if a["id"]}
                result[mapping[k]] = list(rubrics.values())
            else:
                result[mapping[k]] = data[k]

    print(json.dumps(result))




