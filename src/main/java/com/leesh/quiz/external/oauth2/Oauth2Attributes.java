package com.leesh.quiz.external.oauth2;

import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.constant.Oauth2Type;
import com.leesh.quiz.domain.user.constant.Role;

public interface Oauth2Attributes {

    String getName();

    String getEmail();

    String getProfile();

    Oauth2Type getOauth2Type();

    default User toEntity() {

        // 소셜 로그인을 통해 생성된 유저는 기본 권한을 USER로 가진다.
        return User.builder()
                .username(getName())
                .email(getEmail())
                .role(Role.USER)
                .oauth2Type(getOauth2Type())
                .profile(getProfile())
                .build();
    }

}
