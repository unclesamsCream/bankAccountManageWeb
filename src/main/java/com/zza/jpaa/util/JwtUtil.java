package com.zza.jpaa.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zza.jpaa.entity.dto.UserInfo;
import com.zza.jpaa.exception.BizException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class JwtUtil {

    public final  static  Long expire =  30 * 60 * 1000L;

    public static String getToken(UserInfo userInfo) {
        String token = "";
        Date date = new Date();
        date.setTime(date.getTime()+expire);
        token = JWT.create().withAudience(userInfo.getUserId())
                .withClaim("id",userInfo.getUserId())
                .withClaim("userName",userInfo.getName())
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256("password"));
        return token;
    }

    public static String expireAt(String token){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        DecodedJWT decodedJWT  = null;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("password")).build();
        decodedJWT = verifier.verify(token);
        return sdf.format(decodedJWT.getExpiresAt());
    }

    public static Date expireAtDate(String token){
        return JWT.decode(token).getExpiresAt();
    }
    public static UserInfo getUserByToken(String token){
        DecodedJWT decodedJWT  = null;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("password")).build();
        decodedJWT = verifier.verify(token);
        if (decodedJWT.getExpiresAt().before(new Date())){
            throw new BizException("token已过期");
        }
        UserInfo userInfo = UserInfo.builder()
                .userId(decodedJWT.getClaim("id").asString())
                .name(decodedJWT.getClaim("userName").asString())
                .build();
        return userInfo;
    }

    public static void main(String[] args) {
       String token = JWT.create().withAudience("aaaaaaaid")
                .sign(Algorithm.HMAC256("password"));
        Date date = new Date();
        date.setTime(date.getTime()+expire);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        System.out.println(sdf.format(date));
    }
}