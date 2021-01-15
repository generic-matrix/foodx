# ETL

## AIM is the add the data onto the Elastic Search

## Steps
* Download the raw json from https://raw.githubusercontent.com/kz882/recipe/master/recipes_raw_nosource_ar.json
* Run the command

		python3 transform.py /users/Downloads/recipes_raw_nosource_ar.json

A file called data.txt will be generated in the same directory as the python file

* Head over to Kibana and define the mapping structure

		PUT /food
		{
		"mappings": {
			"properties": {
				"title": {
				"type":  "text"
				},
				"ingredients": {
				"type":  "text"
				},
				"instructions": {
				"type":  "text"
				},
				"location": {
				"type": "geo_point"
				}
			}
			}
		}
	
* Now the data.txt push into the /food index as: 

		curl -s -H "Content-Type: application/x-ndjson" -XPOST http://localhost:9200/food/_doc/_bulk?pretty --data-binary @/Users/mb/Desktop/foodx/ETL/data.txt

You can change the diretory as per your machine and change the host as needed