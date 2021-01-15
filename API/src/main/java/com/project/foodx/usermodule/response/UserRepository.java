package com.project.foodx.usermodule.response;

import com.project.foodx.usermodule.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {
    Boolean AddUserToElasticSearch(User user);
    String CheckIfUserExistsInElasticSearch(User user);
    List<AutoComplete> SearchFoodNameElasticSearchForAutoComplete(String food_name);
    Food SearchFoodByIdInElasticSearch(String food_id);
    List<SnapShot> SearchFoodByByGeo(float lat, float lng);
    Boolean AddRating(String foodId, Rating rating);
}
