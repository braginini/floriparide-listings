import json
from time import sleep
from geopy import geocoders
from geopy.exc import GeocoderServiceError
import sys
from mapper import raw_data_dao
from mapper import mappings_reader
from mapper import model_convertor
from mapper import branch_dao


import traceback
import os

g = geocoders.GoogleV3(api_key="AIzaSyDRaIPpzAJu0qubHJ_LMuhH6kM00ocGECM", timeout=300)


def geocode_branch(branch):
    done = False
    attempt = 0
    while not done:
        attempt += 1
        try:

            loc = g.geocode(branch['address'])
            new_address = {}
            # loc2 = g.reverse((b['geometry']['point']['lat'], b['geometry']['point']['lon']))
            if loc:
                if loc.raw['address_components']:
                    for c in loc.raw['address_components']:
                        if 'street_number' in c['types']:
                            new_address['street_number'] = c['long_name']
                        elif 'route' in c['types']:
                            new_address['street'] = c['long_name']
                        elif 'neighborhood' in c['types']:
                            new_address['neighborhood'] = c['long_name']
                        elif 'country' in c['types']:
                            new_address['country'] = c['long_name']
                        elif 'administrative_area_level_1' in c['types']:
                            new_address['state'] = c['long_name']
                        elif 'administrative_area_level_2' in c['types']:
                           new_address['city'] = c['long_name']
                branch['geometry'] = dict(point=loc.raw['geometry']['location'])
                new_address['formatted'] = loc.raw['formatted_address']
                branch['address'] = new_address
            done = True
        except GeocoderServiceError as e:
            print(e)
            sleep(60)
            if attempt >= 3:
                raise GeocoderServiceError(sys.exc_info()[0])
        except Exception as e:
            print(e)
            print(json.dumps(branch, ensure_ascii=False))

    return branch

__author__ = 'Mike'

rootPath = os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir))
rubrics_path = rootPath + r"\data\final_lists\mappings\rubric_hagah_mapping.md"
attrs_path = rootPath + r"\data\final_lists\mappings\attribute_hagah_mapping.md"

hagah_map_path = rootPath + r"\data\final_lists\mappings\branch_model_hagha_model_mapping.md"

rubrics_map = mappings_reader.get_map(rubrics_path)
attrs_map = mappings_reader.get_map(attrs_path)

print("Rubrics map %s" % rubrics_map)
print("Attributes map map %s" % attrs_map)

branches = None
try:
    branches = raw_data_dao.get_by_value_list(raw_data_dao.RawData.CATEGORIES, rubrics_map.keys())
except:
    print(traceback.format_exc())

if branches:
    mapping = mappings_reader.get_map(hagah_map_path)
    branches = [model_convertor.convert_raw_branch(b, mapping, rubrics_map, attrs_map) for b in branches if b]
    print(len(branches))

    branch_set = {}
    dup = 0

    for b in branches:

        if not branch_dao.get_branch(b['name']):
            b = geocode_branch(b)
            if b.get('address'):
                company = branch_dao.get_company(b['name'])
                if company:
                    b['company_id'] = company['id']
                else:
                    b['company_id'] = branch_dao.create_company(b)

                    branch_id = branch_dao.create(b)
            else:
                company = branch_dao.get_company(b['name'])
                if company:
                    b['company_id'] = company['id']
                else:
                    b['company_id'] = branch_dao.create_company(b)
                branch_dao.create(b)










