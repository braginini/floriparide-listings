__author__ = 'mikhail'
import re
import unicodedata


class DaysMapping:
    # each week day followed by all next days list
    days = {"monday": ["monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"],
            "tuesday": ["tuesday", "wednesday", "thursday", "friday", "saturday", "sunday", "monday"],
            "wednesday": ["wednesday", "thursday", "friday", "saturday", "sunday", "monday", "tuesday"],
            "thursday": ["thursday", "friday", "saturday", "sunday", "monday", "tuesday", "wednesday"],
            "friday": ["friday", "saturday", "sunday", "monday", "tuesday", "wednesday", "thursday"],
            "saturday": ["saturday", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday"],
            "sunday": ["sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"]}

    days_mapping = {"segunda": "monday",
                    "terca": "tuesday",
                    "quarta": "wednesday",
                    "quinta": "thursday",
                    "sexta": "friday",
                    "sabado": "saturday",
                    "domingo": "sunday"}


def convert_raw_branch(raw_branch, mapping, rubrics_map, attrs_map):
    """
    converts raw branch to JSON to be sent to admin api
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

    # print(data)
    # print(json.dumps(data))
    result = {}
    for k in mapping.keys():
        if k in data:
            if k == "facilities":
                # We use regexp here to filter out unnecessary characters in facilities names e.g.
                # Tele-entrega(blablabla). In this case regexp will return just Tele-enterga
                attributes = map(lambda v: dict(id=attrs_map.get(re.sub('(\(.*\))', '', v)), value=True), data[k])
                # filter out None
                attributes = {a["id"]: a for a in attributes if a["id"]}
                result[mapping[k]] = list(attributes.values())
            elif k == "categories":
                rubrics = map(lambda v: dict(id=rubrics_map.get(v)), data[k])
                rubrics = {a["id"]: a for a in rubrics if a["id"]}
                result[mapping[k]] = list(rubrics.values())
            else:
                result[mapping[k]] = data[k]

    # print(json.dumps(result))
    result["raw_schedule"] = result.get("schedule")
    result["schedule"] = parse_hours(result.get("schedule"))
    return result


# De segunda a sexta, 9h às 20h. Sábado, das 9h às 19h.
def parse_hours(string):
    if not string:
        return None

    if not string.startswith("De ") and not string.startswith("Diariamente") and ")" in string and "(" in string:
        return None
    #print("Parsing: %s" % string)
    split = string.split(".")
    split = [e.strip(" ") for e in split if e]
    # exclude elements that have no comma like Domingo das 11h às 22h
    split = [e for e in split if "," in e if e]
    #split each group into days->hours dictionary
    dic = {}
    for e in split:
        days_hours = e.split(",")
        days_hours = [el.strip(" ") for el in days_hours]

        #can be Domingo, terca e quinta .... or Domingo, terca, quinta, quarta e sabado etc
        if len(days_hours) > 2:
            i = 0
            while i < len(days_hours) - 1:
                dic[days_hours[i]] = days_hours[len(days_hours) - 1]
                i += 1
        else:
            dic[days_hours[0]] = days_hours[1]

    #now each entry of dictionary has days as a key and hours as a value
    #we have to parse keys and values
    dic = {convert_days(d): convert_hours(h) for d, h in dic.items()}
    #print(dic)
    dic = {d: v for d, v in dic.items() if d and v}
    #key -> week day, value -> list pf hours
    result = {}
    for k, v in dic.items():
        #k - tuple with days
        #v - hours
        for d in k:
            hours_list = result.get(d)
            if not hours_list:
                hours_list = []
                result[d] = hours_list
            for h in v:
                hours_list.append(h)

    return result


def convert_days(raw_days):
    """
    converts draw days string to a tuple of days - strings monday to sunday
    Note! Should return only tuple, because it will be used as a key in a dictionary
    :param raw_days:
    :return:
    """
    # each week day followed by all next days list
    days_mapping = DaysMapping.days_mapping
    days = DaysMapping.days

    #remove umlauts, accents etc
    raw_days = unicodedata.normalize('NFKD', raw_days).encode('ASCII', 'ignore').decode(encoding='UTF-8').lower()
    raw_days = raw_days.replace("-", "").replace("feira", "").replace("de ", "")

    if raw_days == "diariamente":
        return tuple(e for e in days_mapping.values())

    # we go further and split by " a " to get "from" day and "to" day. now raw_days is a list. trim all elements as well
    if " a " in raw_days:
        #split by " a " as here "segunda a sabado"
        raw_days = [e.strip(" ") for e in raw_days.split(" a ") if e]
        #len = 2
        #get days tuple e.g. ("monday", 0)
        day_from = days_mapping.get(raw_days[0])
        day_to = days_mapping.get(raw_days[1])

        if not day_from or not day_to:
            return None

        #Could be terca a terca
        if day_from == day_to:
            return tuple(e for e in days_mapping.values())

        day_list = days[day_from][:(days[day_from].index(day_to) + 1)]
        return tuple(day_list)

    elif " e " in raw_days:
        #split by " e " as here "sabado e domingo"
        raw_days = [e.strip(" ") for e in raw_days.split(" e ") if e]
        day_from = days_mapping.get(raw_days[0])
        day_to = days_mapping.get(raw_days[1])

        if not day_from or not day_to:
            return None
        if day_from == day_to:
            return tuple(e for e in days_mapping.values())

        return tuple([day_from, day_to])
    else:
        #it could be that we have just one day, so we search for it in a dictionary
        day = days_mapping.get(raw_days)
        if day:
            return tuple([day])

    return None


def convert_hours(raw_hours):
    """
    conv
    erts raw hours to map with two entries with keys from and to {"from":"09:00", "to":"24:00"} hh:mm
    :param raw_hours:
    :return:
    """
    # remove umlauts, accents etc
    raw_hours = unicodedata.normalize('NFKD', raw_hours).encode('ASCII', 'ignore').decode(encoding='UTF-8').lower()
    raw_hours = raw_hours.replace("das ", "").strip(" ")
    #print(raw_hours)
    if "partir" in raw_hours or "ultimo" in raw_hours:
        return None
    result = []
    raw_hours = [e.strip(" ") for e in raw_hours.split(" e ")]
    for e in raw_hours:
        e = re.compile(" as | a | ass | ").split(e)
        hour_from = e[0]
        hour_to = e[1]
        if "h" in hour_from and "h" in hour_to:
            hour_from = format_hour(hour_from)
            hour_to = format_hour(hour_to)
            result.append({"from": hour_from, "to": hour_to})
        else:
            result.append({"from": hour_from, "to": hour_to})

    return result


def format_hour(hour):
    """
    formats hour to hh:mm
    :param hour:
    :return:
    """
    hour = hour.split("h")
    result = []
    for h in hour:
        if not h:
            result.append("00")
        elif len(h) == 1:
            result.append("0" + h)
        else:
            result.append(h)

    return ":".join(result)




