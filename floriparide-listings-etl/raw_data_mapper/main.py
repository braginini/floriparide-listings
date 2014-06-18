__author__ = 'Mike'
import raw_data_dao
import mappings_reader
import traceback
import os
import model_convertor
import webapiaccess
import sys
import unicodedata

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
    branches = [model_convertor.convert_raw_branch(b, mapping, rubrics_map, attrs_map) for b in branches]
    print(len(branches))

    branch_set = set()
    dup = 0
    for b in branches:
        if b.get("address"):
            key = b["name"] + b["address"]
            if key not in branch_set:
                webapiaccess.create_branch(b)
                branch_set.add(key)
            else:
                dup += 1
        else:
            webapiaccess.create_branch(b)

    print(dup)










