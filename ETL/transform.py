# Create data.txt to push into elastic search
#!/usr/bin/python

import sys
import json
import random

elasticindex="food"

def ETL(raw_json_dir):
  input_file = open(raw_json_dir, "r")
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
          print("Failed Object : "+str(obj))
  output_file.close()

if __name__ == "__main__":
    if(len(sys.argv)==2):
      ETL(sys.argv[1])
    else:
      print("No valid file dir is passed")