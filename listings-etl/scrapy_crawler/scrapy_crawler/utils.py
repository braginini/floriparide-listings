from collections import Iterable


def get_xpath_value(response, query, default=None):
    value = response.xpath(query).extract()
    if len(value) == 0:
        return default
    return ''.join(value).strip()


def get_css_value(response, query, default=None):
    value = response.css(query).extract()
    if len(value) == 0:
        return default
    return ''.join(value).strip()


def utf2str(data):
    if data:
        if isinstance(data, basestring):
            if isinstance(data, unicode):
                return data.strip().encode('utf-8')
            return data.strip()
        elif isinstance(data, dict):
            return {k: utf2str(v) for k, v in data.iteritems()}
        elif isinstance(data, Iterable):
            return map(utf2str, data)
        return data
