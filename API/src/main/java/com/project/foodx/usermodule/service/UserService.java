package com.project.foodx.usermodule.service;
import com.project.foodx.usermodule.entity.*;
import com.project.foodx.usermodule.response.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Boolean AddNewUser(User user) {
        return userRepository.AddUserToElasticSearch(user);
    }

    public String CheckUserExists(User user) { return userRepository.CheckIfUserExistsInElasticSearch(user); }

    public List<AutoComplete> SearchFood(String food_name) { return userRepository.SearchFoodNameElasticSearchForAutoComplete(food_name); }

    public Food SearchFoodById(String food_id) { return userRepository.SearchFoodByIdInElasticSearch(food_id); }

    public List<SnapShot> SearchFoodByCoord(float lat, float lng) { return userRepository.SearchFoodByByGeo(lat,lng); }

    public Boolean AddRating(String foodId, Rating rating){ return userRepository.AddRating(foodId,rating); }
}
