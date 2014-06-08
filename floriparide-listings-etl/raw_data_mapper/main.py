__author__ = 'Mike'
import raw_data_dao
import read_mappings

rubrics_path = r"C:\Users\Mike\Documents\IdeaProjects\floriparide-listings\floriparide-listings-etl\data\final_lists\mappings\rubric_hagah_mapping.md"

attrs_path = r"C:\Users\Mike\Documents\IdeaProjects\floriparide-listings\floriparide-listings-etl\data\final_lists\mappings\attribute_hagah_mapping.md"

rubrics_map = read_mappings.getmap(rubrics_path)
attrs_map = read_mappings.getmap(attrs_path)

print("Rubrics map %s" % rubrics_map)
print("Attributes map map %s" % attrs_map)

rubrics = raw_data_dao.get_by_value_list(raw_data_dao.RawData.CATEGORIES, rubrics_map.keys())

#todo process rubrics

for rubric in rubrics:
    #todo rubric[1] - json(dictionary)
    print(rubric)