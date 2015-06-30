PUT florianopolis/

{
  "settings": {
    "analysis": {
      "filter": {
        "brazilian_stop": {
          "type":       "stop",
          "stopwords":  "_brazilian_" 
        },
        "brazilian_stemmer": {
          "type":       "stemmer",
          "language":   "brazilian"
        }
      },
      "analyzer": {
        "brazilian": {
          "tokenizer":  "standard",
          "filter": [            
            "lowercase",            
            "brazilian_stop",
            "asciifolding",
            "brazilian_stemmer"
          ]
        }
      }
    },
    "index" : {
        "number_of_shards" : 1,
        "number_of_replicas" : 0
    }
  }
}

PUT florianopolis/_mapping/branch
{
    "branch": {
        "properties": {
            "id": {
                "type": "string",
                "index": "not_analyzed"
            },
            "point": {
                "type": "geo_point"
            },
            "name": {
                "type": "string",
                "boost": 7,
                "index": "analyzed",
                "analyzer": "brazilian",
                "store": "yes"
            },
            "address": {
                "properties": {
                    "street": {
                        "type": "string",
                        "analyzer": "brazilian"
                    },
                    "additional": {
                        "type": "string",
                        "analyzer": "brazilian"
                    },
                    "neighborhood": {
                        "type": "string",
                        "analyzer": "brazilian"
                    }
                }
            },
            "payment_options": {
                "type": "string",
                "analyzer": "brazilian"
            },
            "description": {
                "type": "string",
                "analyzer": "brazilian"
            },
            "tags": {
                "type": "string",
                "analyzer": "brazilian"
            },
            "headline": {
                "type": "string",
                "analyzer": "brazilian"
            },
            "rubrics": {
                "properties": {
                    "id": {
                        "type": "string",
                        "index": "not_analyzed"
                    },
                    "names": {
                        "properties": {
                            "pt_Br": {
                                "type": "string",
                                "analyzer": "brazilian"
                            }
                        }
                    }
                }
            },
            "attributes": {
                "properties": {
                    "id": {
                        "type": "string",
                        "index": "not_analyzed"
                    },
                    "names": {
                        "properties": {
                            "pt_Br": {
                                "type": "string",
                                "analyzer": "brazilian"
                            }
                        }
                    }
                }
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