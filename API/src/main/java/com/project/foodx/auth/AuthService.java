package com.project.foodx.auth;
import com.project.foodx.constants.Constant;
import com.project.foodx.usermodule.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class AuthService {

    //As of now no expirity is set
    public String generateJWTToken(User user) {
        long timestamp = System.currentTimeMillis();
        return Jwts.builder().signWith(SignatureAlgorithm.HS256, Constant.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .compact();
    }
}
