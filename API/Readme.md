# API

## Stack Used
* Spring Boot
* Elastic Search

## Install
    git clone https://github.com/generic-matrix/foodx.git
	cd API
* Open the folder in the IDE your prefer
* In the application.proprties the user index is defined test2 , you can create the index using kibana or cURL as : 
	PUT /test2
* ###### See the  ETL folder to add the data into the elastic search

## Test
Open Postman_Testing.json and import it in Postman

## API Endpoints

* ##### POST /api/v1/signup
##### Body
		{
			"email" : "test2@test.com",
			"password" : "test_password",
			"name": "michael "
		}
###### JWT token will be returned

* ##### GET /api/v1/login
##### Body
		{
			"email" : "test2@test.com",
			"password" : "test_password"
		}
###### JWT token will be returned

##Authorized request
####Pass the Authorization heaer as Bearer and then Token Name
* ##### GET /api/v1/autocomplete?food_name=QueryName

		{
  		"error": null,
  		"data": [
				{
					"id": "34967",
					"name": "Newfie Rice Pudding"
				},
				{
					"id": "35021",
					"name": "Asian Cabbage and Tea Rice"
				}
				
  			]
		}

Data of this structure wil be returned


* ##### GET /api/v1/search?food_id=573

		{
		"error": null,
		"data": {
		"id": "573",
		"title": "Fried Rice Restaurant Style",
		"ingredients": [
			"2 cups enriched white rice ",
			"4 cups water ",
			"2/3 cup chopped baby carrots ",
			"1/2 cup frozen green peas ",
			"2 tablespoons vegetable oil ",
			"2 eggs ",
			"soy sauce to taste ",
			"sesame oil, to taste (optional) ",
			""
		],
		"instructions": [
			"In a saucepan, combine rice and water",
			"Bring to a boil",
			"Reduce heat, cover, and simmer for 20 minutes.In a small saucepan, boil carrots in water about 3 to 5 minutes",
			"Drop peas into boiling water, and drain.Heat wok over high heat",
			"Pour in oil, then stir in carrots and peas; cook about 30 seconds",
			"Crack in eggs, stirring quickly to scramble eggs with vegetables",
			"Stir in cooked rice",
			"Shake in soy sauce, and toss rice to coat",
			"Drizzle with sesame oil, and toss again."
		],
		"rating": [{
				"user_id": "1610685606",
				"user_name": "Kaubi B.",
				"rating_id": "1610707515",
				"message": "Good",
				"star": "4.5"
			},
			{
				"user_id": "1610685606",
				"user_name": "Shayam B.",
				"rating_id": "1610707531",
				"message": "Nice but the bill was heavy",
				"star": "3.7"
			}
		],
		"location": {
			"lat": -33.04752,
			"lng": -71.44249
		}
		}
		}

Data of this structure wil be returned

* ##### GET /api/v1/nearme?lat=-33.04752&lng=-71.44249

		{
		"error": null,
		"data": [{
			"id": "33738",
			"name": "Homemade Peach Tea",
			"rating_count": 0,
			"rating": 0.0
		},
		{
			"id": "573",
			"name": "Fried Rice Restaurant Style",
			"rating_count": 7,
			"rating": 4.5
			}
		]
		}

Data of this structure wil be returned


* ##### POST /api/v1/add-rating
Body

		{
			"food_id":"573",
			"user_name":"Kavin B.",
			"message":"Good",
			"star":"4.5"
		}

Response

		{
			"error": null,
			"data": true
		}
		}

## Security Config (CORS settings) :

The CORS settings in /API/src/main/java/com/project/foodx/FoodxApplication.java the function name is corsFilter ,the header needs to be changed from * to the production url 
