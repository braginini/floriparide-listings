floriparide-listings-api
===========================

Info
-----

This is a catalog API which is used by listings client implemented here [floriparide-listings-client](https://github.com/braginini/floriparide-listings-client.git "floriparide-listings-client") client side. 

Installation and first run
--------------------------

```sh
$ git clone git@github.com:braginini/floriparide-listings.git listings
$ cd listings/floriparide-listings-api
```

Use ```config.py``` to specify DB and WEB Server configuration properties

To run the server use this command:
```sh
$ python main.py
```

Methods
--------
The current API supports a bunch of methods for retrieving catalog information

### Branch search
   ```
   curl -X GET http://162.243.233.204:8888/catalog/1.0/branch/search?q=<query>&start=<start>&limit=<limit>&project_id=<project_id>&locale=<locale>
   ```
   Endpoint accepts the following parameters:
   * ```q``` - the request query; mandatory, string
   * ```start``` - paging, index to return results from; mandatory, int
   * ```limit``` - paging, the size of the result set (if bigger than 1000, will be limited to 1000); mandatory, int
   * ```project_id``` - the id of a project to search branches in; mandatory, bigint
   * ```locale``` - the locale of the client; optional(default pt_Br), string, currently supported values: ru_Ru, pt_Br, en_Us
   
   The response Content-Type is ```application/json;charset=UTF8``` and contains a field ```success``` which indicates whether the request was successful or not (boolean) 
   and field ```result``` with the following top level fields:
   * ```items``` - the list of branches
   * ```total``` - the total number of results found
   * ```markers``` - the list of coordinates for all results found
   * ```top_rubrics``` - the set of top rubrics(ids) for the current results (rubrics that are present in 30% of all results)
   
   Field 
   
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
                    "name": "Villas Café",
                    "address": "Alameda César Nascimento, 322 - Jurerê, Florianopolis - Santa Catarina, 88053-500, Brazil",
                    "geometry": {
                        "point": {
                            "lat": -27.439457,
                            "lon": -48.4907683
                        }
                    },
                    "attributes": null,
                    "rubrics": [
                        {
                            "name": "Cafés / Cafeterias",
                            "id": 9
                        }
                    ],
                    "id": 381
                }
            ],
            "top_rubrics": [
                9,
                16
            ]
        }
    }
```

### Branch get

    Returns a branch object by specified id and project id.
    
    ```
    curl -X GET http://162.243.233.204:8888/catalog/1.0/branch/<project_id>/<branch_id>&locale=<locale>
    ```
    
    Endpoint accepts the following path parameters:
   * ```project_id``` - the id of a project to search branches in; mandatory, bigint
   * ```id``` - the id of a branch to search for, bigint
   
   And one query parameter ```locale``` which indicates the locale of a client; 
   optional(default pt_Br), string, currently supported values: ru_Ru, pt_Br, en_Us
   
   The response Content-Type is ```application/json;charset=UTF8``` and contains a field ```success``` which indicates whether the request was successful or not (boolean) 
   and field ```result``` with the following top level fields:
   
   * ```attributes``` - the list of attributes with nested ```id``` and ```name```(localized) fields
   * ```rubrics``` - the list of rubrics with nested ```id``` and ```name``` (localized) fields
   * ```name``` - the name of a branch
   * ```address``` - the address of a branch
   * ```geometry``` - contains a ```point``` with ```lat``` and ```lon``` of a branch
   
   Example request:
   ``` 
   http://162.243.233.204:8888/catalog/1.0/branch/1/1?locale=pt_Br
   ```
   
   and corresponding response:
   ```
   {
        "result": {
            "address": "SC-401, 10954 - Santo Antonio de Lisboa, Ratones, Florianopolis - Santa Catarina, Brazil",
            "geometry": {
                "point": {
                    "lat": -27.5171001,
                    "lon": -48.5136375
                }
            },
            "rubrics": [
                {
                    "name": "Restaurantes",
                    "id": 16
                }
            ],
            "name": "Meat Shop",
            "id": 1,
            "attributes": [
                {
                    "name": "Estacionamento",
                    "id": 13
                },
                {
                    "name": "Tele-entrega",
                    "id": 15
                },
                {
                    "name": "Ar condicionado",
                    "id": 12
                },
                {
                    "name": "Aceita Reserva",
                    "id": 16
                },
                {
                    "name": "Acessibilidade",
                    "id": 11
                }
            ]
        },
        "success": true
    }
   ``` 

   
   