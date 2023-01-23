package com.leesh.quiz.external.oauth2;

public interface Oauth2ApiService {

    Oauth2Token requestToken(String authorizationCode);

    Oauth2Attributes requestUserInfo(String accessToken);

}
