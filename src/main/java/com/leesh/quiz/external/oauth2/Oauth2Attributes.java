package com.leesh.quiz.external.oauth2;

import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.constant.Oauth2Type;
import com.leesh.quiz.domain.user.constant.Role;

public interface Oauth2Attributes {

    String getName();

    String getEmail();

    String getProfile();

    Oauth2Type getOauth2Type();

}
