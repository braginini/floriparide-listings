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