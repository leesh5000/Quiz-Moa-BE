package com.leesh.quiz.external.oauth2.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leesh.quiz.domain.user.constant.Oauth2Type;
import com.leesh.quiz.external.oauth2.Oauth2Attributes;

public record GoogleUserInfoDto(String name,
                                String email,
                                @JsonProperty("picture") String picture
                             ) implements Oauth2Attributes {

    @Override
    public String getName() {
        return email.split("@")[0];
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getProfile() {
        return picture;
    }

    @Override
    public Oauth2Type getOauth2Type() {
        return Oauth2Type.GOOGLE;
    }
}
