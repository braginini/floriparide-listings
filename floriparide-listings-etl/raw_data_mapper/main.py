__author__ = 'Mike'
import raw_data_dao
import mappings_reader
import traceback
import os
import model_convertor
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

a = u"maçã"
for norm in ('NFC', 'NFKC', 'NFD','NFKD'):
    b = unicodedata.normalize(norm, a)
    print(b, len(b))
model_convertor.parse_hours("De segunda a sexta, 9h às 20h. Sábado, das 9h às 19h.")
model_convertor.parse_hours("De segunda a sexta, das 8h às 19h.")
model_convertor.parse_hours("De Segunda a Sexta, das 08:00 às 12:00 e das 13:30 às 18:00")

branches = None
try:
    branches = raw_data_dao.get_by_value_list(raw_data_dao.RawData.CATEGORIES, rubrics_map.keys())
except:
    print(traceback.format_exc())

if branches:
    mapping = mappings_reader.getmap(hagah_map_path)
    branches = [model_convertor.convert_raw_branch(b, mapping, rubrics_map, attrs_map) for b in branches]
    print(len(branches))







