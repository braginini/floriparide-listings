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

   Returns a branch object by specified id and project id    
    ```
    curl -X GET http://162.243.233.204:8888/catalog/1.0/branch?project_id=<project_id>&branch_id=<branch_id>&locale=<locale>
    ```
    
   Endpoint accepts the following query parameters:
   * ```project_id``` - the id of a project to search branches in; required, bigint
   * ```id``` - the id of a branch to search for, required, bigint
   * ```locale``` - the locale of the client. Optional(default pt_Br), string, currently supported values: ru_Ru, pt_Br, en_Us
   
   The response Content-Type is ```application/json;charset=UTF8``` and contains a field ```success``` which indicates whether the request was successful or not (boolean) 
   and field ```result``` with a field ```items``` which is an array of branch objects (see [Branch object](#branch_obj)).
   
   Example request:
   ``` 
   http://162.243.233.204:8888:8888/catalog/1.0/branch?project_id=1&id=2
   ```
   
   and corresponding response:
   ```
   {
        "success": true,
        "result": {
            "items": [
                {
                    "schedule": {
                        "tuesday": [
                            {
                                "from": "07:00",
                                "to": "21:30"
                            }
                        ],
                        "friday": [
                            {
                                "from": "07:00",
                                "to": "21:30"
                            }
                        ],
                        "sunday": [
                            {
                                "from": "07:00",
                                "to": "21:30"
                            }
                        ],
                        "monday": [
                            {
                                "from": "07:00",
                                "to": "21:30"
                            }
                        ],
                        "saturday": [
                            {
                                "from": "07:00",
                                "to": "21:30"
                            }
                        ],
                        "thursday": [
                            {
                                "from": "07:00",
                                "to": "21:30"
                            }
                        ]
                    },
                    "geometry": {
                        "point": {
                            "lat": -27.4398227,
                            "lon": -48.3877566
                        }
                    },
                    "attributes": [
                        {
                            "id": 15,
                            "name": "Tele-entrega"
                        }
                    ],
                    "contacts": null,
                    "id": 2,
                    "name": "Mercado Pai e Filho",
                    "rubrics": [
                        {
                            "id": 12,
                            "name": "Padarias"
                        }
                    ],
                    "address": "Estrada Dom João Becker, 935 - Ingleses do Rio Vermelho, Florianopolis - Santa Catarina, 88058-600, Brazil"
                }
            ]
        }
    }
   ``` 
   
### Branch get

Returns a list of branch object by specified project id and company 
    ```
    curl -X GET http://162.243.233.204:8888/catalog/1.0/branch/list?project_id=<project_id>&company_id=<company_id>&locale=<locale>&start=<start>&limit=<limit>
    ```
    
   Endpoint accepts the following query parameters:
   * ```project_id``` - the id of a project to search branches in. Required, bigint.
   * ```company_id``` - the id of a company to search in. Required, bigint.
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
   http://162.243.233.204:8888:8888/catalog/1.0/branch/list?project_id=1&company_id=537&start=0&limit=1
   ```
   
   and corresponding response:
   ```
   {
        "success": true,
        "result": {
            "total": 12,
            "items": [
                {
                    "id": 575,
                    "schedule": null,
                    "name": "Bob's Delivery",
                    "geometry": {
                        "point": {
                            "lat": -27.594444,
                            "lon": -48.575278
                        }
                    },
                    "rubrics": [
                        {
                            "id": 11,
                            "name": "Lanchonetes / Fast-food"
                        },
                        {
                            "id": 16,
                            "name": "Restaurantes"
                        }
                    ],
                    "payment_options": null,
                    "contacts": [
                        {
                            "comment": null,
                            "value": "http://www.bobs.com.br/",
                            "contact": "website"
                        }
                    ],
                    "address": "Estreito, Florianopolis - Santa Catarina, Brazil",
                    "attributes": [
                        {
                            "id": 15,
                            "name": "Tele-entrega"
                        }
                    ]
                }
            ],
            "markers": [
                {
                    "lat": -27.594444,
                    "name": "Bob's Delivery",
                    "branch_id": 575,
                    "lon": -48.575278
                },
                {
                    "lat": -27.5838969,
                    "name": "Bob's",
                    "branch_id": 537,
                    "lon": -48.5448969
                },
                {
                    "lat": -27.5766349,
                    "name": "Bob's",
                    "branch_id": 563,
                    "lon": -48.5312279
                },
                {
                    "lat": -27.5909356,
                    "name": "Quiosque Bob's",
                    "branch_id": 593,
                    "lon": -48.5074708
                },
                {
                    "lat": -27.5965572,
                    "name": "Quiosque Bob's",
                    "branch_id": 598,
                    "lon": -48.5462377
                },
                {
                    "lat": -27.595278,
                    "name": "Quiosque Bob's",
                    "branch_id": 599,
                    "lon": -48.596111
                },
                {
                    "lat": -27.5766349,
                    "name": "Quiosque Bob's",
                    "branch_id": 600,
                    "lon": -48.5312279
                },
                {
                    "lat": -27.5838969,
                    "name": "Quiosque Bob's",
                    "branch_id": 724,
                    "lon": -48.5448969
                },
                {
                    "lat": -27.5966746,
                    "name": "Bobs",
                    "branch_id": 863,
                    "lon": -48.5539083
                }
            ]
        }
    }
   ``` 
   
### <a name="branch_obj"></a>Branch object

   * ```attributes``` - an array of attributes (see [Attribute object](#attribute_obj)). Required.
   * ```rubrics``` - an array of rubrics (see [Rubric object](#rubric_obj)). Required.
   * ```name``` - a name of a branch. Required.
   * ```address``` - an address of a branch.Required.
   * ```geometry``` - a geo location of a branch. Contains a ```point``` with ```lat``` and ```lon``` of a branch. Required.
   * ```contacts``` - an array of contact information of a branch (see [Contact object](#contact_obj)). Required.
   * ```schedule``` - an array of week days working hours of a branch (see [Schedule object](#schedule_obj)). If no week day specified consider that branch doesn't work this day. Each day can contain multiple schedule objects. Required.  
   * ```payment_options``` - an array of payment options accepted by branch(see [Payment options object](#payment_options_obj) for a full list of possible values). Required. 
   
### <a name="contact_obj">Contact object 
 
   * ```contact``` - the type of a contact (could be ```phone```, ```website```, ```email```, ```skype```, ```fax```, ```jabber```, ```twitter```. Required.   
   * ```value``` - the value of the contact (e.g. for type ```phone``` the value could be (48)32349548. Required.
   * ```comment``` - the additional comment of a contact. Optional.
 
### <a name="attribute_obj">Attribute object 
   * ```name``` - a name of an attribute (localized by request parameter  ```phone``` locale) .Required.   
   * ```id``` - a unique id of an attribute
   
### <a name="rubric_obj">Rubric object 
   * ```name``` - a name of rubric (localized by request parameter  ```phone``` locale) .Required.   
   * ```id``` - a unique id of a rubric   
   
### <a name="schedule_obj">Schedule object 
   * ```from``` - a time when the branch starts to work on this day ```hh:MM```. Required.   
   * ```to``` - a time when the branch closes on this day ```hh:MM```. Required.    
   
### <a name="marker_obj">Marker object 
   * ```branch_id``` - an id of a corresponding branch
   * ```name``` - a name of the marker(branch) to display
   * ```lat``` - a latitude of the marker to display on map
   * ```lon``` - a longitude of the marker to display on map
   
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