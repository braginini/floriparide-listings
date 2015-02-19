PUT florianopolis/
{
    "settings": {
        "analysis": {
            "analyzer": {
                "index_analyzer": {
                    "tokenizer": "standard",
                    "filter": ["standard", "lowercase", "stop", "asciifolding", "porter_stem"]
                },
                "search_analyzer": {
                    "tokenizer": "standard",
                    "filter": ["standard", "lowercase", "stop", "asciifolding", "porter_stem"]
                }
            }
        },
        "index" : {
            "number_of_shards" : 1,
            "number_of_replicas" : 1
        }
    }
}

PUT florianopolis/branch/_mapping
{
"branch": {
      "_all" : {"enabled" : true, "index_analyzer": "index_analyzer", "search_analyzer": "search_analyzer"},
      "properties": {
        "id": {
          "type": "string",
          "index": "not_analyzed"
        },
        "name": {
          "type": "string",
          "boost": 7.0,
          "index": "analyzed",
          "index_analyzer": "index_analyzer",
          "search_analyzer": "search_analyzer",
          "store": "yes"
        }
      }
    }
}

#river conf
PUT /_river/my_jdbc_river/_meta
{
    "type" : "jdbc",
    "jdbc" : {
        "url" : "jdbc:postgresql://localhost:5432/floriparide_listings",
        "user" : "postgres",
        "password" : "postgres",
        "sql" : "select id as _id, name, data  from public.branch",
        "index" : "florianopolis",
        "type" : "branch"
    }
}