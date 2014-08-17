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
   * ```items``` - a list of branches (see [Branch object](#branch_obj)).
   * ```total``` - the total number of results found
   * ```markers``` - a list of coordinates for all results found (see [Marker object](#marker_obj)).
   * ```top_rubrics``` - a set of top rubrics(ids) for the current results (rubrics that are present in 30% of all results)
    
   
   **Note that ```markers``` and ```top_rubrics``` will be returned only for the first page (if ```start``` = 0).**
   
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

   Returns an array of branch object by specified id and project id    
    ```
    curl -X GET http://162.243.233.204:8888/catalog/1.0/branch?project_id=<project_id>&branch_id=<branch_id>&locale=<locale>
    ```
    
   Endpoint accepts the following query parameters:
   * ```project_id``` - the id of a project to search branches in; required, bigint
   * ```id``` - a list of comma-separated branch ids
   * ```locale``` - the locale of the client. Optional(default pt_Br), string, currently supported values: ru_Ru, pt_Br, en_Us
   
   The response Content-Type is ```application/json;charset=UTF8``` and contains a field ```success``` which indicates whether the request was successful or not (boolean) 
   and field ```result``` with a field ```items``` which is an array of branch objects (see [Branch object](#branch_obj)) and ```total``` number of results found
   
   Example request:
   ``` 
   http://162.243.233.204:8888/catalog/1.0/branch?project_id=1&id=22,333
   ```
   
   and corresponding response:
   ```
   {
        "result": {
            "items": [
                {
                    "name": "Pantumaka",
                    "geometry": {
                        "point": {
                            "lat": -27.6034961,
                            "lon": -48.4644175
                        }
                    },
                    "schedule": null,
                    "attributes": null,
                    "contacts": null,
                    "payment_options": null,
                    "id": 22,
                    "address": "Travessa Leopoldo João Santos, 51 - Lagoa da Conceicao, Florianopolis - Santa Catarina, Brazil",
                    "rubrics": [
                        {
                            "name": "Padarias",
                            "id": 12
                        }
                    ]
                },
                {
                    "name": "Lost Cyber Café",
                    "geometry": {
                        "point": {
                            "lat": -27.5965572,
                            "lon": -48.5462377
                        }
                    },
                    "schedule": {
                        "friday": [
                            {
                                "to": "22:00",
                                "from": "07:30"
                            }
                        ],
                        "thursday": [
                            {
                                "to": "22:00",
                                "from": "07:30"
                            }
                        ],
                        "monday": [
                            {
                                "to": "22:00",
                                "from": "07:30"
                            }
                        ],
                        "tuesday": [
                            {
                                "to": "22:00",
                                "from": "07:30"
                            }
                        ],
                        "sunday": [
                            {
                                "to": "22:00",
                                "from": "07:30"
                            }
                        ],
                        "saturday": [
                            {
                                "to": "22:00",
                                "from": "07:30"
                            }
                        ]
                    },
                    "attributes": [
                        {
                            "name": "Aceita Reserva",
                            "id": 16
                        },
                        {
                            "name": "Acessibilidade",
                            "id": 11
                        }
                    ],
                    "contacts": null,
                    "payment_options": [
                        "visa",
                        "mastercard",
                        "maestro",
                        "visa electron",
                        "redeshop",
                        "mastercard maestro"
                    ],
                    "id": 333,
                    "address": "Centro, Florianopolis - Santa Catarina, Brazil",
                    "rubrics": [
                        {
                            "name": "Cafés / Cafeterias",
                            "id": 9
                        }
                    ]
                }
            ],
            "total": 2
        },
        "success": true
    }
   ``` 
   
### Branch list

Returns a list of branch object by specified project id and company id and/or rubric id 

    ```
    curl -X GET http://162.243.233.204:8888/catalog/1.0/branch/list?project_id=<project_id>&company_id=<company_id>&rubric_id=<rubric_id>&locale=<locale>&start=<start>&limit=<limit>
    ```
    
   Endpoint accepts the following query parameters:
   * ```project_id``` - the id of a project to search branches in. Required, bigint.
   * ```company_id``` - the id of a company to search in. Optional(if not specified, rubric_id must be specified), bigint.
   * ```rubric_id``` - the id of a rubric to search in. Optional(if not specified company_id must be specified), bigint.
   * ```start``` - the start index for paging. Required, int.
   * ```limit``` - the size of the result to return for paging. Required, int.
   * ```locale``` - the locale of the client. Optional(default pt_Br), string, currently supported values: ru_Ru, pt_Br, en_Us
   
   The response Content-Type is ```application/json;charset=UTF8``` and contains a field ```success``` which indicates whether the request was successful or not (boolean) 
   and field ```result``` with the following top level fields:
   * ```items``` - an array of branches (see [Branch object](#branch_obj)).
   * ```total``` - the total number of results found
   * ```markers``` - a list of coordinates for all results found (see [Marker object](#marker_obj)).
   
   Example request:
   ``` 
   http://162.243.233.204:8888/catalog/1.0/branch/list?project_id=1&start=0&limit=10&rubric_id=16&company_id=1
   ```
   
   and corresponding response:
   ```
   {
        "success": true,
        "result": {
            "total": 1,
            "markers": [
                {
                    "lon": -48.5136375,
                    "name": "Meat Shop",
                    "lat": -27.5171001,
                    "branch_id": 1
                }
            ],
            "items": [
                {
                    "payment_options": [
                        "american express",
                        "visa",
                        "mastercard",
                        "diners",
                        "redeshop",
                        "visa electron",
                        "mastercard maestro",
                        "visa vale alimentação"
                    ],
                    "address": "SC-401, 10954 - Santo Antonio de Lisboa, Ratones, Florianopolis - Santa Catarina, Brazil",
                    "contacts": [
                        {
                            "contact": "phone",
                            "comment": null,
                            "value": "(48)32349548"
                        },
                        {
                            "contact": "phone",
                            "comment": null,
                            "value": "(48)99718299"
                        }
                    ],
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
                    ],
                    "geometry": {
                        "point": {
                            "lon": -48.5136375,
                            "lat": -27.5171001
                        }
                    },
                    "rubrics": [
                        {
                            "name": "Restaurantes",
                            "id": 16
                        }
                    ],
                    "name": "Meat Shop",
                    "schedule": null,
                    "id": 1
                }
            ]
        }
    }
   ``` 
   
   
### Project get

Returns a list of project objects by specified ids

    ```
    curl -X GET http://162.243.233.204:8888/catalog/1.0/project?id=<id>&locale=<locale>
    ```
    
   Endpoint accepts the following query parameters:
   * ```id``` - comma-separated list of project ids to get 
   * ```locale``` - the locale of the client. Optional(default pt_Br), string, currently supported values: ru_Ru, pt_Br, en_Us
   
   The response Content-Type is ```application/json;charset=UTF8``` and contains a field ```success``` which indicates whether the request was successful or not (boolean) 
   and field ```result``` with the following top level fields:
   * ```items``` - an array of projects (see [Project object](#project_obj)).
   * ```total``` - the total number of results found
   
   Example request:
   ``` 
   http://floriparide.com.br:8888/catalog/1.0/project/list?id=0&locale=pt_br
   ```
   
   and corresponding response:
   
   ```
    {
        "result": {
            "items": [
                {
                    "name": "Florianópolis",
                    "language": "pt",
                    "locale": "pt_br",
                    "default_position": {
                        "lat": 43.2384784,
                        "lon": 76.9452985,
                        "zoom": 11
                    },
                    "time_zone": {
                        "utc_offset": "-03:00",
                        "name": "Brasília time"
                    },
                    "bounds": "POLYGON((82.5066233234198 55.249037769223,83.3965344871157 55.249037769223,83.3965344871157 54.5530320570113,82.5066233234198 54.5530320570113,82.5066233234198 55.249037769223))",
                    "id": 0,
                    "zoom": {
                        "max": 19,
                        "min": 11
                    }
                }
            ],
            "total": 1
        },
        "success": true
    }
    ```
   
### Project list

Returns a list of project objects

    ```
    curl -X GET http://162.243.233.204:8888/catalog/1.0/project/list?locale=<locale>
    ```
    
   Endpoint accepts the following query parameters:
   * ```locale``` - the locale of the client. Optional(default pt_Br), string, currently supported values: ru_Ru, pt_Br, en_Us
   
   The response Content-Type is ```application/json;charset=UTF8``` and contains a field ```success``` which indicates whether the request was successful or not (boolean) 
   and field ```result``` with the following top level fields:
   * ```items``` - an array of projects (see [Project object](#project_obj)).
   * ```total``` - the total number of results found
   
   Example request:
   ``` 
   http://floriparide.com.br:8888/catalog/1.0/project/list?locale=pt_br
   ```
   
   and corresponding response:
   
   ```
    {
        "result": {
            "items": [
                {
                    "name": "Florianópolis",
                    "language": "pt",
                    "locale": "pt_br",
                    "default_position": {
                        "lat": 43.2384784,
                        "lon": 76.9452985,
                        "zoom": 11
                    },
                    "time_zone": {
                        "utc_offset": "-03:00",
                        "name": "Brasília time"
                    },
                    "bounds": "POLYGON((82.5066233234198 55.249037769223,83.3965344871157 55.249037769223,83.3965344871157 54.5530320570113,82.5066233234198 54.5530320570113,82.5066233234198 55.249037769223))",
                    "id": 0,
                    "zoom": {
                        "max": 19,
                        "min": 11
                    }
                }
            ],
            "total": 1
        },
        "success": true
    }
    ```

### <a name="project_obj">Project object 
   * ```id``` - a unique id of a project
   * ```language``` - language of a project
   * ```name``` - name of a project
   * ```locale``` - locale of a project
   * ```default_position``` - default position to center map to with a latitude ```lat```, longitude ```lon``` and zoom level ```zoom```
   * ```time_zone``` - timezone of a project with utc offset ```utc_offset``` and time zone name ```name``` 
   * ```bounds``` - geometrical bounds of a project in WKT format
   * ```zoom``` - available zoom levels of a project with min value ```min``` and maximum value ```max```
   
   
### <a name="branch_obj"></a>Branch object

   * ```attributes``` - array of attributes (see [Attribute object](#attribute_obj)). Required.
   * ```rubrics``` - array of rubrics (see [Rubric object](#rubric_obj)). Required.
   * ```name``` - name of a branch. Required.
   * ```address``` - address of a branch.Required.
   * ```geometry``` - geo location of a branch. Contains a ```point``` with ```lat``` and ```lon``` of a branch. Required.
   * ```contacts``` - array of contact information of a branch (see [Contact object](#contact_obj)). Required.
   * ```schedule``` - array of week days working hours of a branch (see [Schedule object](#schedule_obj)). If no week day specified consider that branch doesn't work this day. Each day can contain multiple schedule objects. Required.  
   * ```payment_options``` - array of payment options accepted by branch(see [Payment options object](#payment_options_obj) for a full list of possible values). Required. 
   
### <a name="contact_obj">Contact object 
 
   * ```contact``` - type of a contact (could be ```phone```, ```website```, ```email```, ```skype```, ```fax```, ```jabber```, ```twitter```. Required.   
   * ```value``` - value of the contact (e.g. for type ```phone``` the value could be (48)32349548. Required.
   * ```comment``` - additional comment of a contact. Optional.
 
### <a name="attribute_obj">Attribute object 
   * ```name``` - name of an attribute (localized by request parameter  ```phone``` locale) .Required.   
   * ```id``` - unique id of an attribute
   
### <a name="rubric_obj">Rubric object 
   * ```name``` - name of rubric (localized by request parameter  ```phone``` locale) .Required.   
   * ```id``` - unique id of a rubric   
   
### <a name="schedule_obj">Schedule object 
   * ```from``` - time when the branch starts to work on this day ```hh:MM```. Required.   
   * ```to``` - time when the branch closes on this day ```hh:MM```. Required.    
   
### <a name="marker_obj">Marker object 
   * ```branch_id``` - id of a corresponding branch
   * ```name``` - name of the marker(branch) to display
   * ```lat``` - latitude of the marker to display on map
   * ```lon``` - longitude of the marker to display on map
   
### <a name="payment_options_obj">Payment options object 
   * ```american express```
   * ```visa```
   * ```mastercard```
   * ```diners```
   * ```redeshop```
   * ```visa electron```
   * ```mastercard maestro```
   * ```visa vale alimentação```
   
   //todo complete the list