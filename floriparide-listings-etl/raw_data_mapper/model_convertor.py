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

    #print(data)
   # print(json.dumps(data))
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

    #print(json.dumps(result))
    parse_hours(result.get("schedule"))
    return result

#De segunda a sexta, 9h às 20h. Sábado, das 9h às 19h.
def parse_hours(string):
    if not string:
        return

    if not string.startswith("De ") and not string.startswith("Diariamente") and ")" in string and "(" in string:
        return
    print("Parsing: %s" % string)
    split = string.split(".")
    split = [e.strip(" ") for e in split if e]
    #exclude elements that have no comma like Domingo das 11h às 22h
    split = [e for e in split if "," in e if e]
    #split each group into days->hours dictionary
    dic = {}
    for e in split:
        days_hours = e.split(",")
        days_hours = [el.strip(" ") for el in days_hours]
        dic[days_hours[0]] = days_hours[1]

    #now each entry of dictionary has days as a key and hours as a value
    #we have to parse keys and values
    dic = {convert_days(d): convert_hours(h) for d, h in dic.items()}
    print(dic)


def convert_days(raw_days):
    """
    converts draw days string to a tuple of days - strings monday to sunday
    Note! Should return only tuple, because it will be used as a key in a map
    :param raw_days:
    :return:
    """
    days = {"segunda": ("monday", 0),
            "terca": ("tuesday", 1),
            "quarta": ("wednesday", 2),
            "quinta": ("thursday", 3),
            "sexta": ("friday", 4),
            "sabado": ("saturday", 5),
            "domingo": ("sunday", 6)}

    days_indexed = {0: "monday",
                    1: "tuesday",
                    2: "wednesday",
                    3: "thursday",
                    4: "friday",
                    5: "saturday",
                    6: "sunday"}
    #remove umlauts, accents etc
    raw_days = unicodedata.normalize('NFKD', raw_days).encode('ASCII', 'ignore').decode(encoding='UTF-8').lower()
    raw_days = raw_days.replace("-", "").replace("feira", "").replace("de ", "")

    if raw_days == "diariamente":
        return tuple(e[0] for e in days.values())

    # we go further and split by " a " to get "from" day and "to" day. now raw_days is a list. trim all elements as well
    if " a " in raw_days:
        #split by " a " as here "segunda a sabado"
        raw_days = [e.strip(" ") for e in raw_days.split(" a ") if e]
        #len = 2
        day_from = days.get(raw_days[0])
        day_to = days.get(raw_days[1])
        if day_from and day_to:
            day_list = []
            day_from = day_from[1]
            day_to = day_to[1]
            while day_from <= day_to:
                day_list.append(days_indexed.get(day_from))
                day_from += 1
            return tuple(day_list)
    elif " e " in raw_days:
        #split by " e " as here "sabado e domingo"
        raw_days = [e.strip(" ") for e in raw_days.split(" e ") if e]
        day_from = days.get(raw_days[0])
        day_to = days.get(raw_days[1])
        if day_from and day_to:
            return tuple([day_from[0], day_to[0]])
    else:
        #it could be that we have just one day, so we search for it in a dictionary
        day = days.get(raw_days[0])
        if day:
            return day[0]

    #todo Parsing: De domingo a quinta, das 18h30 a 0h. Sexta e sábado, das 18h30 a 0h30.
    #todo {('friday', 'saturday'): 'das 18h30 a 0h30', (): 'das 18h30 a 0h'}
    return None


def convert_hours(raw_hours):
    """
    conv
    erts raw hours to map with two entries with keys from and to {"from":"09:00", "to":"24:00"} hh:mm
    :param raw_hours:
    :return:
    """
    return raw_hours





