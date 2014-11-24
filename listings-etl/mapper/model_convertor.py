import logging

__author__ = 'mikhail'
import re
import unicodedata

check_is_hour_pattern = re.compile('\d')
week_days_pattern = 'segunda|terca|quinta|quarta|sexta|sabado|domingo'

week_days = ['segunda',
             'terca',
             'quarta',
             'quinta',
             'sexta',
             'sabado',
             'domingo']

days_mapping = {'segunda': 'monday',
                'terca': 'tuesday',
                'quarta': 'wednesday',
                'quinta': 'thursday',
                'sexta': 'friday',
                'sabado': 'saturday',
                'domingo': 'sunday'}


def convert_raw_branch(raw_branch, mapping, rubrics_map, attrs_map):
    """
    converts raw branch to JSON to be sent to admin api
    :param raw_branch: raw branch object as a dictionary
    :param mapping: mapping for raw data source
    :param rubrics_map: mapping for rubrics
    :param attrs_map: mapping for attributes
    :return: returns a json object for a rubric to be ready to be sent to admin api
    """

    if raw_branch.get('source') == 'hagah':
        return hagah_raw_branch(raw_branch.get('data'), mapping, rubrics_map, attrs_map)


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
    if '\'' in data['name']:
        data['name'] = data['name'].replace('\'', '\'\'')
    for k in mapping.keys():
        if k in data:
            if k == 'attributes' and data.get('attributes'):
                # We use regexp here to filter out unnecessary characters in facilities names e.g.
                # Tele-entrega(blablabla). In this case regexp will return just Tele-enterga
                attributes = map(lambda v: dict(id=attrs_map.get(re.sub('(\(.*\))', '', v)), value=True), data[k])
                # filter out None
                attributes = {a['id']: a for a in attributes if a['id']}
                result[mapping[k]] = list(attributes.values())
            elif k == 'categories' and data.get('categories'):
                rubrics = map(lambda v: dict(id=rubrics_map.get(v)), data[k])
                rubrics = {a['id']: a for a in rubrics if a['id']}
                result[mapping[k]] = list(rubrics.values())
            else:
                result[mapping[k]] = data.get(k)

    # print(json.dumps(result))
    result['schedule'] = parse_hours(data.get('working_hours'))

    if not result['schedule'] and data.get('working_hours'):
        logging.warning('No working hours parsed: \'%s\'' % str(data.get('working_hours')))

    result['raw'] = data
    result['payment_options'] = dict(credit_cards=result.get('credit_cards'),
                                     debit_cards=result.get('debit_cards'), food_cards=result.get('food_cards'),
                                     other=result.get('other_payment_methods'))

    del result['credit_cards']
    del result['debit_cards']
    del result['food_cards']
    del result['other_payment_methods']

    return result  # De segunda a sexta, 9h às 20h. Sábado, das 9h às 19h.


def parse_hours(string):
    days = []
    hours = []
    result = {}

    def flush():
        nonlocal days, hours
        result_hours = []
        # format to from, to
        for i in range(0, int(len(hours) / 2)):
            result_hours.append({'from': hours[i * 2], 'to': hours[i * 2 + 1]})

        # when no days were found, but we have hours
        if not days:
            days = day_interval_iterator('segunda', 'domingo')

        for d in days:
            result[days_mapping[d]] = result_hours

        days = []
        hours = []

    for g in parse_hours_group(string):
        # determine if the group is days or hours group
        if not check_is_hour_pattern.search(g):

            if days:
                if hours:
                    flush()

            tmp_days = re.findall(week_days_pattern, g)
            # check if the result days list is interval or not
            if len(tmp_days) == 1:
                days.append(tmp_days[0])
            elif len(tmp_days) == 2:
                for d in day_interval_iterator(tmp_days[0], tmp_days[1]):
                    days.append(d)
            else:
                if 'diariamente' == g:
                    for d in day_interval_iterator('segunda', 'domingo'):
                        days.append(d)
                else:
                    pass
                    # logging.warning('crazy line %s %s' % (str(string), str(tmp_days)))
        else:
            g = g.replace(':', '').replace('h', '')
            tmp_hours = re.findall('\d+', g)
            tmp_hours = list(map(format_hours, tmp_hours))
            if g.startswith('a partir') and len(tmp_hours) == 1:
                tmp_hours.append('00:00')
            hours += tmp_hours

    flush()

    # remove empty days (non working)
    result = {d: h for d, h in result.items() if h}

    return result


def format_hours(raw_hour):
    """
    formats raw hour to HH:MM
    5 possible situations 9, 12, 930, 1200, abnormal
    :param raw_hour:
    :return:
    """
    if raw_hour:
        if len(raw_hour) == 1:
            # 1st situation we add 0 to left and 00 to right
            raw_hour = '%s%s%s' % ('0', str(raw_hour), '00')
        elif len(raw_hour) == 2:
            # 2nd situation - we add 00 to the right
            raw_hour = '%s%s' % (str(raw_hour), '00')
        elif len(raw_hour) == 3:
            # 3d situation - we add one zero to the left
            raw_hour = '%s%s' % ('0', str(raw_hour))
        elif len(raw_hour) > 4:
            # 5th situation - abnormal
            logging.warning('Crazy hour %s' % raw_hour)
            return None
        # add colon : to hour, eg 0900 -> 09:00
        return '%s:%s' % (raw_hour[0:2], raw_hour[2:])
    return None


def day_interval_iterator(from_d, to_d):
    """
    returns all days from from_d (src day) to to_d (dest day)
    :param from_d:
    :param to_d:
    :return:
    """
    idx = week_days.index(from_d)
    to_idx = week_days.index(to_d)
    while idx != to_idx:
        yield week_days[idx]
        if idx == len(week_days) - 1:
            idx = 0
        else:
            idx += 1

    yield week_days[idx]


def parse_hours_group(string):
    if not string:
        return None

    # sometimes we have already splitted groups, but sometimes not - have to split
    if isinstance(string, list):
        for g in map(parse_hours_group, string):
            yield from g
    else:
        # normalize to ASCII characters
        string = string.strip('Â ')
        string = string.replace('ç', 'c')
        string = string.replace('à', 'a')
        string = unicodedata.normalize('NFKD', string).encode('ASCII', 'ignore').decode(encoding='UTF-8').lower()
        string = string.replace(' e ', ',')

        string = re.split('\r\n|\.', string)

        # split group by comma to get days + hours, trim each result
        for g in string:
            if g:
                for s in g.split(','):
                    yield s.strip()


parse_hours(
    'De segunda a sexta, das 10h às 23h. ')

