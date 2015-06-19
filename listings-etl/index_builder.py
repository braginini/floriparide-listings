from mapper import branch_dao
import json
from elasticsearch import Elasticsearch
__author__ = 'Mike'

branches = branch_dao.get_all()
es = Elasticsearch(hosts=['104.131.54.232:9992'])
for b in branches:
    data = b.get("draft")
    data["name"] = b.get("name")
    data["id"] = b.get("id")
    print(es.index(index="florianopolis", doc_type="branch", id=data["id"], body=json.dumps(data)))
