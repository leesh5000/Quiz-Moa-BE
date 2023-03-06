package com.leesh.quiz.external.oauth2.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leesh.quiz.domain.user.constant.Oauth2Type;
import com.leesh.quiz.external.oauth2.Oauth2Attributes;

public record NaverUserInfoDto(@JsonProperty("result_code")
                                    String resultCode, String message, Response response) implements Oauth2Attributes {


    public record Response(String id, String nickname, String email, String name,
                           @JsonProperty("profile_image") String profileImage) {

    }

    @Override
    public String getName() {
        return response.email.split("@")[0];
    }

    @Override
    public String getEmail() {
        return response.email;
    }

    @Override
    public String getProfile() {
        return response.profileImage;
    }

    @Override
    public Oauth2Type getOauth2Type() {
        return Oauth2Type.NAVER;
    }

}
