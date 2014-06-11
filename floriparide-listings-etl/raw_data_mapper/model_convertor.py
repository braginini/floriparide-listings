__author__ = 'mikhail'
import json
import re
import unicodedata


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

#De segunda a sexta, 9h às 20h. Sábado, das 9h às 19h.
def parse_hours(string):
    if not string:
        return

    print("Parsing: %s" % string)
    string = string.strip(" .")
    print(string)
    first_split = string.split(".")
    first_split = [el.strip(" ") for el in first_split]
    dic = map(lambda e: dict(days=e.split(",")[0].strip(" "), hours=e.split(",")[1].strip(" ")), first_split)
    #make a dictionary with key - days and value - hours
    dic = {a["days"]: a["hours"] for a in dic}
    dic = {(k.replace("De ", ""), v.replace("das ", "")) for k, v in dic.items()}
    for k, v in dic:
        #todo parse hours (interval) as a map from to
        day_split = k.split(" a ")
        day1 = get_day_number(day_split[0])
        if len(day_split) > 1:
           day2 = get_day_number(day_split[1])

        #todo for each day starting from day1 to day2

def get_day_number(raw_day):
    """
    Searches the corresponding day no matter how it is written
    return an integer from 1 to 7 for corresponding week day
    :param raw_day:
    :return:
    """
    days = {"segunda": 1, "terca": 2, "quarta": 3, "quinta": 4, "sexta": 5, "sabado": 6, "domingo": 7}
    raw_day = raw_day.lower()

    if raw_day in days:
        return days[raw_day]

    #raw_day = raw_day.replace("á", "a")
    #raw_day = raw_day.replace("ç", "c")
    #raw_day = re.sub("feira|-", "", raw_day)

    return days.get(raw_day)





