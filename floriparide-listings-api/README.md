floriparide-listings-api
===========================

__Info:__

This is a catalog API which is used by listings client implemented here [floriparide-listings-client](https://github.com/braginini/floriparide-listings-client.git "floriparide-listings-client") client side. 

__Install and run:__

```sh
$ git clone git@github.com:braginini/floriparide-listings.git listings
$ cd listings/floriparide-listings-api
```

Use ```config.py``` to specify DB and WEB Server configuration properties

To run the server use this command:
```sh
$ python main.py
```

__Methods:__
The current API supports a bunch of methods:

1. Branch search:
   ```
   GET http://162.243.233.204:8888/catalog/1.0/branch/search?q=<query>&start=<start>&limit=<limit>&project_id=<project_id>&locale=<locale>
   ```
   Endpoint accepts the following parameters:
   * ```q``` - the request query; mandatory, string
   * ```start``` - paging, index to return results from; mandatory, int
   * ```limit``` - paging, the size of the result set (if bigger than 1000, will be limited to 1000); mandatory, int
   * ```project_id``` - the id of a project to search branches in; mandatory, bigint
   * ```locale``` - the locale of the client; optional(default pt_Br), string, currently supported values: ru_Ru, pt_Br, en_Us
   
   The response Content-Type is ```application/json;charset=UTF8``` and contains a field ```result``` with the following top level fields:
   * ```items``` - the list of branches
   * ```total``` - the total number of results found
   * ```markers``` - the list of coordinates for all results found
   * ```top_rubrics``` - the set of top rubrics(ids) for the current results (rubrics that are present in 30% of all results)
   
   Each ```items``` entry has the following structure:
   * ```attributes``` - the list of attributes with nested ```id``` and ```name```(localized) fields
   * ```rubrics``` - the list of rubrics with nested ```id``` and ```name``` (localized) fields
   * ```name``` - the name of a branch
   * ```address``` - the address of a branch
   * ```geometry``` - contains a ```point``` with ```lat``` and ```lon``` of a branch
   
   Each ```markers``` entry has the following structure:
   * ```branch_id``` - the id of a corresponding branch
   * ```name``` - the name of the marker(branch) to display
   * ```lat``` - the latitude of the marker to display on map
   * ```lon``` - the longitude of the marker to display on map
   
   Note that ```markers``` and ```top_rubrics``` will be returned only for the first page (if ```start``` = 0).
   
   Example request:
   ``` 
   http://162.243.233.204:8888/catalog/1.0/branch/search?q=cafe&start=0&limit=1&project_id=1  
   ```
   
   and corresponding response:
   ```
   {
        "result": {
            "total": 2,
            "markers": [
                {
                    "branch_id": 1,
                    "lon": -48.5136375,
                    "name": "Meat Shop",
                    "lat": -27.5171001
                },
                {
                    "branch_id": 8,
                    "lon": -48.50553110000001,
                    "name": "Amoratto Drive Thru",
                    "lat": -27.4945225
                },
                {
                    "branch_id": 10,
                    "lon": -48.4714615,
                    "name": "Floripão",
                    "lat": -27.6339339
                }
            ],
            "items": [
                {
                    "id": 1,
                    "rubrics": null,
                    "attributes": null,
                    "name": "Meat Shop",
                    "geometry": {
                        "point": {
                            "lon": -48.5136375,
                            "lat": -27.5171001
                        }
                    },
                    "address": "SC-401, 10954 - Santo Antonio de Lisboa, Ratones, Florianopolis - Santa Catarina, Brazil"
                }
            ],
            "top_rubrics": [
                9,
                16
            ]
        }
    }
```
   
   