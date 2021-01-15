#PUT /food
# curl -s -H "Content-Type: application/x-ndjson" -XPOST http://localhost:9200/food/_doc/_bulk?pretty --data-binary @/Users/kaustubh/Desktop/foodx/ETL/data.txt

'''
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
'''

import json
import random

elasticindex="food"
input_file = open("recipes_raw_nosource_ar.json", "r")
cities_json = open("cities.json", "r")
raw=input_file.read()
raw_cities=cities_json.read()
input_file.close()
cities_json.close()
raw=raw.replace("ADVERTISEMENT","")
input_object = json.loads(raw)
cities = json.loads(raw_cities)
output_file= open("data.txt","w+")


counter=0
city_length=len(cities)
for key in input_object:
    obj=input_object[key]
    try:
        if(obj!={}):
            obj["instructions"]=obj["instructions"].replace("\n","").split(". ")
            del obj["picture_link"]
            index=random.randint(0,city_length)
            city=cities[index]
            obj["location"]={
                "lat":float(city["lat"]),
                "lon":float(city["lng"])
            }
            output_file.write(json.dumps({"index":{"_index":elasticindex,"_id":counter}})+"\n")
            output_file.write(json.dumps(obj)+"\n")
            counter=counter+1
    except:
        print(obj)

output_file.close()