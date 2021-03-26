/* *
 * This sample demonstrates handling intents from an Alexa skill using the Alexa Skills Kit SDK (v2).
 * Please visit https://alexa.design/cookbook for additional examples on implementing slots, dialog management,
 * session persistence, api calls, and more.
 * */
const Alexa = require('ask-sdk-core');
const foodxApi=require("./foodx-api/adapter.js");
const {Food}=require("./foodx-api/food.js");
let food=new Food("");
const LaunchRequestHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'LaunchRequest';
    },
    async handle(handlerInput) {
        const { accessToken } = handlerInput.requestEnvelope.session.user;
        let speakOutput = '';
        if(accessToken===undefined){
            speakOutput = 'Welcome to foodX , please link your account so you can access35k+ recepies .';
        }else{
            if (typeof accessToken !== "undefined") {
                const info = await foodxApi.GetUserInfo(accessToken);
                
                if(info.error==null){
                   speakOutput = `Hey ${info.data.name},You can ask me how to cook a recepie like pizza, I will guide you the best.`;
                   food=new Food(info.data.token);
                }else{
                    speakOutput = 'Sorry ,'+info.error;
                }
            } else {
                speakOutput = 'Internal Error ! ';
            }
        }
        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};




const HelloWorldIntentHandler = {
    canHandle(handlerInput) {
            return  Alexa.getIntentName(handlerInput.requestEnvelope) === 'GetRecepieIntent';
    },
    handle(handlerInput) {
        const speakOutput = 'Cook';

        return handlerInput.responseBuilder
            .speak(speakOutput)
            //.reprompt('add a reprompt if you want to keep the session open for the user to respond')
            .getResponse();
    }
};

const HelpIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'AMAZON.HelpIntent';
    },
    handle(handlerInput) {
        const speakOutput = 'You can say hello to me! How can I help?';

        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

const CancelAndStopIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && (Alexa.getIntentName(handlerInput.requestEnvelope) === 'AMAZON.CancelIntent'
                || Alexa.getIntentName(handlerInput.requestEnvelope) === 'AMAZON.StopIntent');
    },
    handle(handlerInput) {
        const speakOutput = 'Goodbye!';

        return handlerInput.responseBuilder
            .speak(speakOutput)
            .getResponse();
    }
};
/* *
 * FallbackIntent triggers when a customer says something that doesn’t map to any intents in your skill
 * It must also be defined in the language model (if the locale supports it)
 * This handler can be safely added but will be ingnored in locales that do not support it yet 
 * */
const FallbackIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'AMAZON.FallbackIntent';
    },
    handle(handlerInput) {
        //clarify the uers on what you can ask .....
        const speakOutput = 'Sorry, I don\'t know about that. Please try again.';

        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};
/* *
 * SessionEndedRequest notifies that a session was ended. This handler will be triggered when a currently open 
 * session is closed for one of the following reasons: 1) The user says "exit" or "quit". 2) The user does not 
 * respond or says something that does not match an intent defined in your voice model. 3) An error occurs 
 * */
const SessionEndedRequestHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'SessionEndedRequest';
    },
    handle(handlerInput) {
        console.log(`~~~~ Session ended: ${JSON.stringify(handlerInput.requestEnvelope)}`);
        // Any cleanup logic goes here.
        return handlerInput.responseBuilder.getResponse(); // notice we send an empty response
    }
};
/* *
 * The intent reflector is used for interaction model testing and debugging.
 * It will simply repeat the intent the user said. You can create custom handlers for your intents 
 * by defining them above, then also adding them to the request handler chain below 
 * */
const IntentReflectorHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest';
    },
    async handle(handlerInput) {
        const intentName = Alexa.getIntentName(handlerInput.requestEnvelope);
        var speakOutput ="";
         
        if(intentName==="GetRecepie"){
            if (handlerInput.requestEnvelope.request.intent.slots.food_name === undefined) {
                speakOutput = "Hmm ! Sorry , I didn't get the food name , can you rephrase it ? ";
            } else {
                let input="";
                if(handlerInput.requestEnvelope.request.intent.slots.food_name.value!==undefined){
                    input=handlerInput.requestEnvelope.request.intent.slots.food_name.value.toLowerCase();
                }
                if(handlerInput.requestEnvelope.request.intent.slots.food_name.slotValue.values!==undefined){
                    for(var i=0;i<handlerInput.requestEnvelope.request.intent.slots.food_name.slotValue.values.length;i++){
                        if(i==handlerInput.requestEnvelope.request.intent.slots.food_name.slotValue.values.length-1){
                            input=input+handlerInput.requestEnvelope.request.intent.slots.food_name.slotValue.values[i].value.toLowerCase();
                        }else{
                            input=input+handlerInput.requestEnvelope.request.intent.slots.food_name.slotValue.values[i].value.toLowerCase()+" "
                        }
                    }
                }
                await food.GetRecepie(input).then((response)=>{
                    speakOutput =  response;
                }).catch((error)=>{
                     speakOutput = error;
                })
            }
        }
        
        if(intentName==="GetIngredients"){
            speakOutput=food.GetAllIngredients();
        }
        
        if(intentName==="AllSteps"){
            speakOutput=food.AllSteps();
        }
        
        if(intentName==="NextStep"){
            speakOutput=food.NextStep();
        }
        
        if(intentName==="PreviousStep"){
            speakOutput=food.PreviousStep();
        }

        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};
/**
 * Generic error handling to capture any syntax or routing errors. If you receive an error
 * stating the request handler chain is not found, you have not implemented a handler for
 * the intent being invoked or included it in the skill builder below 
 * */
const ErrorHandler = {
    canHandle() {
        return true;
    },
    async handle(handlerInput, error) {
        let input="";
        if(handlerInput.requestEnvelope.request.intent.slots.food_name.slotValue.values!==undefined){
            for(var i=0;i<handlerInput.requestEnvelope.request.intent.slots.food_name.slotValue.values.length;i++){
                if(i==handlerInput.requestEnvelope.request.intent.slots.food_name.slotValue.values.length-1){
                    input=input+handlerInput.requestEnvelope.request.intent.slots.food_name.slotValue.values[i].value.toLowerCase();
                }else{
                    input=input+handlerInput.requestEnvelope.request.intent.slots.food_name.slotValue.values[i].value.toLowerCase()+" "
                }
            }
        }
        if(input.length>1){
            await food.GetRecepie(input).then((response)=>{
                    speakOutput =  response;
                }).catch((error)=>{
                     speakOutput = error;
                })
        }else{
            speakOutput = "Sorry, I din't get the context ,you may ask how to cook sandwich or any other recipie ,try it !  ";
        }
        console.log(`~~~~ Error handled: ${JSON.stringify(error)}`);

        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

/**
 * This handler acts as the entry point for your skill, routing all request and response
 * payloads to the handlers above. Make sure any new handlers or interceptors you've
 * defined are included below. The order matters - they're processed top to bottom 
 * */
exports.handler = Alexa.SkillBuilders.custom()
    .addRequestHandlers(
        LaunchRequestHandler,
        HelloWorldIntentHandler,
        HelpIntentHandler,
        CancelAndStopIntentHandler,
        FallbackIntentHandler,
        SessionEndedRequestHandler,
        IntentReflectorHandler)
    .addErrorHandlers(
        ErrorHandler)
    .addRequestInterceptors(function (handlerInput) {
    console.log(`\n********** REQUEST *********\n${JSON.stringify(handlerInput, null, 4)}`);
})
.addResponseInterceptors(function (request, response) {
    console.log(`\n************* RESPONSE **************\n${JSON.stringify(response, null, 4)}`);
})
    .withCustomUserAgent('sample/hello-world/v1.2')
    .lambda();