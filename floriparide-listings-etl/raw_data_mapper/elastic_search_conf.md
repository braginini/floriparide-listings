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