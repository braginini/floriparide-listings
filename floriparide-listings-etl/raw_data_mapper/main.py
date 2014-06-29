from time import sleep
from geopy import geocoders
from geopy.exc import GeocoderServiceError
import sys

__author__ = 'Mike'
import raw_data_dao
import mappings_reader
import traceback
import os
import model_convertor
import webapiaccess

rootPath = os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir))
rubrics_path = rootPath + r"\data\final_lists\mappings\rubric_hagah_mapping.md"
attrs_path = rootPath + r"\data\final_lists\mappings\attribute_hagah_mapping.md"

hagah_map_path = rootPath + r"\data\final_lists\mappings\branch_model_hagha_model_mapping.md"

rubrics_map = mappings_reader.getmap(rubrics_path)
attrs_map = mappings_reader.getmap(attrs_path)

print("Rubrics map %s" % rubrics_map)
print("Attributes map map %s" % attrs_map)

branches = None
try:
    branches = raw_data_dao.get_by_value_list(raw_data_dao.RawData.CATEGORIES, rubrics_map.keys())
except:
    print(traceback.format_exc())

if branches:
    mapping = mappings_reader.getmap(hagah_map_path)
    branches = [model_convertor.convert_raw_branch(b, mapping, rubrics_map, attrs_map) for b in branches if b]
    print(len(branches))

    branch_set = set()
    dup = 0
    f = open("geocoded", "ab")
    for b in branches:
        b["raw_address"] = b.get("address")
        if b.get("address"):
            #get the geocoding info for an address
            g = geocoders.GoogleV3(api_key="AIzaSyC3pYl4E8bwR6OLtGWZOh6etjjtcNyN-Cw", timeout=300)
            done = False
            attempt = 0
            while not done:
                attempt += 1
                try:
                    loc = g.geocode(b["address"])
                    if loc:
                        place, (lat, lon) = str(loc), (loc.latitude, loc.longitude)
                        b["address"] = place
                        b["lat"] = lat
                        b["lon"] = lon
                        print(loc)
                    done = True
                except GeocoderServiceError:
                    sleep(60)
                    if attempt >= 3:
                        raise GeocoderServiceError(sys.exc_info()[0])
            key = b["name"] + b["address"]
            if key not in branch_set:
                branch_id = webapiaccess.create_branch(b)
                branch_set.add(key)
                #write to file to save the state
                line = "%s;%s;%s;%s;%s\n" % (b.get("name"), str(branch_id), b.get("address"), b.get("lat"), b.get("lon"))
                f.write(line.encode("utf-8"))
                f.flush()
            else:
                dup += 1
        else:
            webapiaccess.create_branch(b)

    f.close()
    print(dup)










