from time import sleep
from geopy import geocoders
from geopy.exc import GeocoderServiceError
import sys
from mapper import raw_data_dao
from mapper import mappings_reader
from mapper import model_convertor
from mapper import webapi_access
from mapper import branch_dao


import traceback
import os

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

    branch_set = set()
    dup = 0

    #geo_map = geocoded_reader.getmap("C://Users//mikhail//Documents//IdeaProjects//floriparide-listings//"
    #                                "floriparide-listings-etl//raw_data_mapper//geocoded")

    f = open("geocoded", "ab")
    for b in branches:
        if b.get("address"):
            #get the geocoding info for an address
            g = geocoders.GoogleV3(api_key="AIzaSyArI9wIPFW9iaMJHxdPzh7bGceb6C3Oef8", timeout=300)
            done = False
            attempt = 0
            while not done:
                attempt += 1
                try:
                    loc = g.geocode(b["address"])
                    if loc:
                        place, (lat, lng) = str(loc), (loc.latitude, loc.longitude)
                        b["address"] = place
                        b["lat"] = lat
                        b["lng"] = lng
                        print(loc)
                    done = True
                except GeocoderServiceError:
                    sleep(60)
                    if attempt >= 3:
                        raise GeocoderServiceError(sys.exc_info()[0])
            key = b["name"] + b["address"]
            if key not in branch_set:
                company = branch_dao.get_company(b['name'])
                if company:
                    b['company_id'] = company['id']
                else:
                    b['company_id'] = branch_dao.create_company(b)

                branch_id = branch_dao.create(b)
                branch_set.add(key)
                #write to file to save the state
                line = "%s;%s;%s;%s;%s;%s\n" % (b.get("name"), str(branch_id), b.get("address"), b.get("raw_address"),
                                                b.get("lat"), b.get("lng"))
                f.write(line.encode("utf-8"))
                f.flush()
            else:
                dup += 1
        else:
            company = branch_dao.get_company(b['name'])
            if company:
                b['company_id'] = company['id']
            else:
                b['company_id'] = branch_dao.create_company(b)
            branch_dao.create(b)

    f.close()
    print(dup)










