const https = require('https');

async function getUserInfo(accessToken) {
    return new Promise((resolve, reject) => {
        const options = {
            "method": "GET",
            "hostname": "api.trygistify.com",
            "path": "/api/v1/alexa?token="+Buffer.from(accessToken).toString('base64')
        };
        let req = https.request(options, (response) => {
            let returnData = '';

            response.on('data', (chunk) => {
                returnData += chunk;
            });

            response.on('end', () => {
                resolve(JSON.parse(returnData));
            });

            response.on("error", (error) => {
                reject(error)
            })
        })
        req.end();
    })
}


async function AutoComplete(value){
    return new Promise((resolve, reject) => {
          const options = {
            "method": "GET",
            "rejectUnauthorized": false,
            "hostname": "api.trygistify.com",
            "path": "/api/v1/autocomplete?food_name="+encodeURIComponent(value)
        };
        let req = https.request(options, (response) => {
            let returnData = '';

            response.on('data', (chunk) => {
                returnData += chunk;
            });

            response.on('end', () => {
                resolve(JSON.parse(returnData));
            });

            response.on("error", (error) => {
                reject(error)
            })
        })
        req.end();
    });
    
}


async function FoodById(id,token){
    //Add a API to check if the user exists by the AWS alexa skill token and calback with the token ...
    return new Promise((resolve, reject) => {
          const options = {
            "method": "GET",
            "rejectUnauthorized": false,
            "hostname": "api.trygistify.com",
            "path": "/api/v1/search?food_id="+id,
            "headers": {
                "Authorization": `Bearer ${"eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MTEwNDAzNTksImlkIjoiMTYxMDc4NjMzOSIsImVtYWlsIjoidGVzdDJAdGVzdC5jb20ifQ.mFrWeNPtRUQD576lBR_kx7sqFuKDBqdAA9GmZjZ_Tso"}`
            }
        };
        let req = https.request(options, (response) => {
            let returnData = '';

            response.on('data', (chunk) => {
                returnData += chunk;
            });

            response.on('end', () => {
                resolve(JSON.parse(returnData));
            });

            response.on("error", (error) => {
                reject(error)
            })
        })
        req.end();
    });
    
}

module.exports={
    "AutoComplete": AutoComplete,
    "FoodById": FoodById,
    "GetUserInfo":getUserInfo
}