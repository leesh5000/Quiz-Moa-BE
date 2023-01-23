package com.leesh.quiz.external.oauth2.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leesh.quiz.domain.user.constant.Oauth2Type;
import com.leesh.quiz.external.oauth2.Oauth2Attributes;

public record GoogleUserInfoResponse(String name,
                                     String email,
                                     @JsonProperty("picture") String picture
                             ) implements Oauth2Attributes {

    @Override
    public String getName() {
        return name;
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
    public String getOauth2Type() {
        return Oauth2Type.GOOGLE.name();
    }
}
