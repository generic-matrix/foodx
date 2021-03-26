var pos = require('pos');
const host="https://api.trygistify.com";
function Login(email,password){   
    return new Promise(function(resolve,reject){
        fetch(host+'/api/v1/login', {  
            method: 'post',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
              "email" : email,
              "password" : password
            }),
        })
        .then(response => {
            if(response.status===200){
                response.json().then((data)=>{
                    if(data.error===null){
                        //set the state to dashboard
                        resolve(data.data.token);
                    }else{
                        reject(data.error);
                    }
                });
            }else{
                reject("-->Internal Error")
            }
        })
        .catch((error) => reject("Internal Error"));
    })
}

function SignUp(name,email,password){   
    return new Promise(function(resolve,reject){
        fetch(host+'/api/v1/signup', {  
            method: 'post',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
            "name": name,
              "email" : email,
              "password" : password,
            })
        })
        .then(response => {
            if(response.status===200){
                response.json().then((data)=>{
                    if(data.error===null){
                        //set the state to dashboard
                        resolve(data.data.token);
                    }else{
                        reject(data.error);
                    }
                });
            }else{
                reject("Internal Error")
            }
        })
        .catch((error) => reject("Internal Error"));
    })
}


function AutoComplete(query,Cookies){   
    return new Promise(function(resolve,reject){
        fetch(host+'/api/v1/autocomplete?food_name='+query, {  
            method: 'GET',
            headers: {'Authorization':"Bearer "+Cookies.get('token')}
        })
        .then(response => {
            if(response.status===200){
                response.json().then((data)=>{
                    if(data.error===null){
                        //set the state to dashboard
                        resolve(data.data);
                    }else{
                        reject(data.error);
                    }
                });
            }else{
                reject("Internal Error")
            }
        })
        .catch((error) => reject("Internal Error"));
    })
}

function GetFoodId(results,value){
    let res=null;
    results.forEach(obj => {
        if(obj.name===value){
            res=obj.id;
        }
    });
    return res;
}

function GetIP(){
    return new Promise(function(resolve,reject){
        fetch("https://www.cloudflare.com/cdn-cgi/trace", {  
            method: 'GET'
        })
        .then(response => {
            if(response.status===200){
                response.text().then((data)=>{
                    resolve(data.split("\n")[2].replace("ip=",""))
                }).catch((error)=>{
                    reject("Internal Error")
                })
            }else{
                reject("Internal Error")
            }
        })
        .catch((error) => reject("Internal Error"));
    })
}

function GetNouns(sentence){
    var string="";
    var words = new pos.Lexer().lex(sentence);
    var tagger = new pos.Tagger();
    var taggedWords = tagger.tag(words);
    for (var i in taggedWords) {
        var taggedWord = taggedWords[i];
        var word = taggedWord[0];
        var tag = taggedWord[1];
        if(tag==="NN" || tag==="NNP" || tag==="NNS"){
            string=string+word+",";
        }
    }
    return string;
}

function GetNearbyFoods(lat,lng,Cookies){   
    return new Promise(function(resolve,reject){
        fetch(host+'/api/v1/nearme?lat='+lat+'&lng='+lng, {  
            method: 'GET',
            headers: {'Authorization':"Bearer "+Cookies.get('token')}
        })
        .then(response => {
            if(response.status===200){
                response.json().then((data)=>{
                    if(data.error===null){
                        //set the state to dashboard
                        resolve(data.data);
                    }else{
                        reject(data.error);
                    }
                });
            }else{
                reject("Internal Error")
            }
        })
        .catch((error) => reject("Internal Error"));
    })
}


function GetFoodDetails(food_id,Cookies){
    return new Promise(function(resolve,reject){
        fetch(host+'/api/v1/search?food_id='+food_id, {  
            method: 'GET',
            headers: {'Authorization':"Bearer "+Cookies.get('token')}
        })
        .then(response => {
            if(response.status===200){
                response.json().then((data)=>{
                    if(data.error===null){
                        //set the state to dashboard
                        resolve(data.data);
                    }else{
                        reject(data.error);
                    }
                });
            }else{
                reject("Internal Error")
            }
        })
        .catch((error) => reject("Internal Error"));
    })
}


function AddRating(food_id,user_name,message,star,Cookies){
    return new Promise(function(resolve,reject){
        fetch(host+'/api/v1/add-rating', {  
            method: 'POST',
            headers: {
                'Authorization':"Bearer "+Cookies.get('token'),
                "Content-Type":"application/json"
            },
            body: JSON.stringify({
                "food_id":food_id,
                "user_name":user_name,
                "message":message,
                "star":""+star+""
            })
        })
        .then(response => {
            if(response.status===200){
                response.json().then((data)=>{
                    console.log(JSON.stringify(data))
                    if(data.error===null){
                        //set the state to dashboard
                        resolve("okay");
                    }else{
                        reject(data.error);
                    }
                });
            }else{
                reject("Internal Error")
            }
        })
        .catch((error) => reject("Internal Error"));
    })
}

function parseJwt (token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
};


module.exports={
    "Login":Login,
    "SignUp":SignUp,
    "AutoComplete":AutoComplete,
    "GetFoodId":GetFoodId,
    "GetIP":GetIP,
    "GetNearbyFoods":GetNearbyFoods,
    "GetNouns":GetNouns,
    "GetFoodDetails":GetFoodDetails,
    "AddRating":AddRating,
    "parseJwt":parseJwt
}