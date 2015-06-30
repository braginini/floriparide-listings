GET florianopolis/branch/_search
{
    "query": {
        "filtered": {
            "query": {
                "match_phrase": {
                    "_all": "cafe"
                }
            },
            "filter": {
                "bool": {
                    "must": [
                        
                        {
                            "term": {
                                "payment_options.option": ["visa", "mastercard"]
                            }
                        }
                    ]
                }
            }
        }
    }
}

#search with attributes filter
POST florianopolis/branch/_search
{
  "query": {
    "filtered": { 
      "query": {
            "match_phrase": {
                "_all": "cafe"
            }
        },
      "filter": {
            "term": {
                "attributes.id": "13"
            }
        }
    }
  }
}

#search with attributes filter, multiple values - IN clause - any of the attributes
POST florianopolis/branch/_search
{
  "query": {
    "filtered": { 
      "query": {
            "match_phrase": {
                "_all": "cafe"
            }
        },
      "filter": {
            "term": {
                "attributes.id": ["13", "11", "12"]
            }
        }
    }
  }
}

#search with attributes and rubric filter, multiple values - AND clause - all attributes and rubrics should match
POST florianopolis/branch/_search
{
    "query": {
        "filtered": {
            "query": {
                "match_phrase": {
                    "_all": "cafe"
                }
            },
            "filter": {
                "and": [
                    {
                        "term": {
                            "attributes.id": "13"
                        }
                    },
                    {
                        "term": {
                            "attributes.id": "12"
                        }
                    },
                    {
                        "term": {
                            "rubrics.id": "19"
                        }
                    }
                ]
            }
        }
    }
}

#search with attributes and rubric filter, multiple values - AND clause and with defined specific attribute value
POST test/test/_search
{
    "query": {
        "filtered": {
            "filter": {
                "and": [
                    {
                        "and": [
                            {
                                "term": {
                                    "attributes.id": "13"
                                }
                            },
                            {
                                "term": {
                                    "attributes.value": true
                                }
                            }
                        ]
                    },
                    {
                        "term": {
                            "rubrics.id": "19"
                        }
                    }
                ]
            }
        }
    }
}

#search with attributes and rubric filter, multiple values - AND clause and with defined specific attribute value and another structure of index
#where attribute id is a key and value is a value
POST test/test/_search
{
    "query": {
        "filtered": {
            "filter": {
                "and": [
                    {
                        "term": {
                            "rubrics.id": "1"
                        }
                    },
                    {
                        "term": {
                            "attributes.14": true
                        }
                    }
                ]
            }
        }
    }
}

#search query with query param and 2 attributes as filters
POST florianopolis/branch/_search
{
    "query": {
        "filtered": {
            "filter": {
                "and": [
                    {
                        "and": [
                            {
                                "term": {
                                    "attributes.id": "15"
                                }
                            },
                            {
                                "term": {
                                    "attributes.value": true
                                }
                            }
                        ]
                    },
                    {
                        "and": [
                            {
                                "term": {
                                    "attributes.id": "13"
                                }
                            },
                            {
                                "term": {
                                    "attributes.value": true
                                }
                            }
                        ]
                    }
                ]
            },
            "query": {
                "match_phrase": {
                    "_all": "restaurantes"
                }
            }
        }
    },
    "from": 0
}