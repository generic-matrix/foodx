package com.project.foodx.controller;

import java.util.List;
import com.project.foodx.auth.AuthService;
import com.project.foodx.response.ResponseObject;
import com.project.foodx.usermodule.entity.*;
import com.project.foodx.usermodule.service.UserService;
import com.project.foodx.usermodule.service.Util;
import net.minidev.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value ="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject login(@RequestBody User requestUserDetails) {
        User userDto = new User();
        BeanUtils.copyProperties(requestUserDetails, userDto);
        if(userDto.getEmail()==null || userDto.getPassword()==null){
            return new ResponseObject("Email or password payload missing", null);
        }else {
            String user_id = userService.CheckUserExists(userDto);
            if(user_id==null){
                return new ResponseObject("No such user exists", null);
            }else{
                userDto.setId(user_id);
                AuthService auth=new AuthService();
                JSONObject obj = new JSONObject();
                obj.put("token",auth.generateJWTToken(userDto));
                return new ResponseObject(null,obj);
            }
        }
    }

    @PostMapping(value ="/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject createUser(@RequestBody User requestUserDetails) {
        User userDto = new User();
        BeanUtils.copyProperties(requestUserDetails, userDto);
        if(userDto.getEmail()==null || userDto.getPassword()==null){
            return new ResponseObject("Email or password payload missing", null);
        }else {
            String user_id = userService.CheckUserExists(userDto);
            if(user_id==null) {
                userDto.setRandomId();
                Boolean success = userService.AddNewUser(userDto);
                if (success) {
                    JSONObject obj = new JSONObject();
                    AuthService auth=new AuthService();
                    obj.put("token",auth.generateJWTToken(userDto));
                    return new ResponseObject(null, obj);
                } else {
                    return new ResponseObject("Internal Error", null);
                }
            }else{
                return new ResponseObject("User already exists", null);
            }
        }
    }

    @GetMapping(value = "/autocomplete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject SearchFood(@RequestParam String food_name) {
        if(food_name.length()>0){
            List<AutoComplete> data= userService.SearchFood(food_name);
            return new ResponseObject(null,data);
        }else {
            return new ResponseObject("food name missing", null);
        }
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject SearchFoodById(@RequestParam String food_id) {
        //int userId = (Integer) request.getAttribute("id");
        if(food_id.length()>0){
            Food data= userService.SearchFoodById(food_id);
            return new ResponseObject(null,data);
        }else {
            return new ResponseObject("food id missing", null);
        }
    }


    @GetMapping(value = "/nearme", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject SearchFoodByCoord(@RequestParam String lat,@RequestParam String lng) {
        try {
            if(lat.length()>0 && lng.length()>0) {
                float latitute = Float.parseFloat(lat);
                float longitude = Float.parseFloat(lng);
                return new ResponseObject(null, userService.SearchFoodByCoord(latitute,longitude));
            }else{
                return new ResponseObject("Latitude or longitude is missing ", null);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return new ResponseObject("Internal Error", null);
        }
    }

    //String user_id,String message, int star
    @PostMapping(value = "/add-rating", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject AddRating(HttpServletRequest request,@RequestBody RequestObject req_obj) {
        try {
            String userId = Integer.toString((Integer) request.getAttribute("id"));
            Rating rating = new Rating(userId, req_obj.getUser_name(), new Util().setRandomId(), req_obj.getMessage(), req_obj.getStar());
            return new ResponseObject(null, userService.AddRating(req_obj.getFood_id(), rating));
        }catch (Exception ex){
            ex.printStackTrace();
            return new ResponseObject(null,"Malformed Inputs");
        }
    }
}
