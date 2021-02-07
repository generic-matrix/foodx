const Adapter=require("./adapter.js");

class Food{
    constructor(token){
        this.token=token;
        this.stepCount=0;
    }
    
    async GetRecepie(value){
        return new Promise(async (resolve, reject) => {
            try{
                //https://api.trygistify.com/api/v1/autocomplete?food_name=value
                const json=await Adapter.AutoComplete(value);
                if(json===undefined || json===null){
                    reject("Sorry some Internal Error,please try again ! ");
                }else{
                    //if count ===0 return string link didnt understand as it may not be in the data base please try again  
                    if(json.data.length===0){
                        reject("Sorry we din't found any food like that, can you say it again ? ");
                    }
                    //get the 1st element id 
                    this.id=json.data[0].id;
                    this.name=json.data[0].name;
                    //https://api.trygistify.com/api/v1/search?food_id=
                    const food_raw_json=await Adapter.FoodById(this.id,this.token);
                    //if error!==null return bummer we got some internal error please try again
                    if(food_raw_json===undefined || food_raw_json===null){
                        reject("Sorry some Internal Error,please try again ! ");
                    }
                    if(food_raw_json.error!==null){
                        reject("Sorry some Internal Error,please try again ! ");
                    }
                    this.steps=food_raw_json.data.instructions;
                    this.ingredients=food_raw_json.data.ingredients;
                    resolve("Okay so , lets start making "+this.name+". You will need "+this.GetAllIngredients()+"  and the first step is "+this.steps[this.stepCount]+" . Let me know once this step is done , so you can say next step or prior step or show all steps to get more info. Let's do it !");
                }
            }catch(error){
                reject("Sorry some Internal Error,please try again ! ");
            }
        });
    }
    
    GetAllIngredients(){
        //return this.ingredients in string
        let result="We need total "+this.ingredients.length+" Ingredients \n ";
        for(var i=0;i<this.ingredients.length;i++){
            if(i==this.ingredients.length-1){
                result=result+this.ingredients[i];
            }else{
                result=result+this.ingredients[i]+",";
            }
        }
        return result;
    }
    
    NextStep(){
        if(this.steps===undefined){
            return "You can ask me how to cook a recepie like pizza, I will guide you the best."
        }
        this.stepCount=this.stepCount+1;
        if(this.stepCount<this.steps.length){
            return this.steps[this.stepCount]
        }else{
            return "This was the last step , we believe the food must be ready to eat now ."
        }
    }
    
    PreviousStep(){
        if(this.steps===undefined){
            return "You can ask me how to cook a recepie like pizza, I will guide you the best."
        }
        this.stepCount=this.stepCount-1;
        if(this.stepCount>-1){
            return this.steps[this.stepCount]
        }else{
            return "This was the first  step , you can say next step so I will tell you the next step if needed ."
        }
    }
    
    AllSteps(){
            if(this.steps===undefined){
                return "You can ask me how to cook a recepie like pizza, I will guide you the best."
            }
            //return this.ingredients in string
            let result="There are total "+this.steps.length+" Steps to cook \n ";
            for(var i=0;i<this.steps.length;i++){
                if(i==this.steps.length-1){
                    result=result+this.steps[i];
                }else{
                    result=result+this.steps[i]+",";
                }
            }
            return result;
    }
}

module.exports={
    Food:Food
}