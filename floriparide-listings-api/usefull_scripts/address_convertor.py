import json
from time import sleep

from geopy import geocoders
from geopy.exc import GeocoderServiceError

import sys
import requests
import dao

__author__ = 'mikhail'

branches = dao.branch_dao.get_entity()
g = geocoders.GoogleV3(api_key="AIzaSyArI9wIPFW9iaMJHxdPzh7bGceb6C3Oef8", timeout=300)
# g = geocoders.MapQuest('Fmjtd%7Cluub2501ng%2C2a%3Do5-9uzl00', scheme='http', timeout=300)


def geocode_branch(b):
    done = False
    attempt = 0
    while not done:
        attempt += 1
        try:
            if 'raw_address' not in b['data']:
                return b

            loc = g.geocode(b['data']['raw_address'])
            b['data']['address'] = {}
            # loc2 = g.reverse((b['data']['geometry']['point']['lat'], b['data']['geometry']['point']['lon']))
            if loc:
                if loc.raw['address_components']:
                    for c in loc.raw['address_components']:
                        if 'street_number' in c['types']:
                            b['data']['address']['street_number'] = c['long_name']
                        elif 'route' in c['types']:
                            b['data']['address']['street'] = c['long_name']
                        elif 'neighborhood' in c['types']:
                            b['data']['address']['neighborhood'] = c['long_name']
                        elif 'country' in c['types']:
                            b['data']['address']['country'] = c['long_name']
                        elif 'administrative_area_level_1' in c['types']:
                            b['data']['address']['state'] = c['long_name']
                        elif 'administrative_area_level_2' in c['types']:
                            b['data']['address']['city'] = c['long_name']
                b['data']['geometry']['point'] = loc.raw['geometry']['location']
                b['data']['address']['formatted'] = loc.raw['formatted_address']
            done = True
            dao.branch_dao.update(b['id'], 'data', json.dumps(b['data'], ensure_ascii=False), True)
        except GeocoderServiceError as e:
            print(e)
            sleep(60)
            if attempt >= 3:
                raise GeocoderServiceError(sys.exc_info()[0])
        except Exception as e:
            print(e)
            print(b['id'] + ' ' + json.dumps(b['data'], ensure_ascii=False))

    return b


branches = [geocode_branch(b) for b in branches]

print(branches)

