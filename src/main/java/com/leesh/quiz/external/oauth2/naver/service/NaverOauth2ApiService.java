package com.leesh.quiz.external.oauth2.naver.service;

import com.leesh.quiz.external.oauth2.Oauth2ApiService;
import com.leesh.quiz.external.oauth2.Oauth2Attributes;
import com.leesh.quiz.external.oauth2.Oauth2Token;
import com.leesh.quiz.external.oauth2.naver.client.NaverApiClient;
import com.leesh.quiz.external.oauth2.naver.client.NaverAuthClient;
import com.leesh.quiz.external.oauth2.naver.dto.NaverOauth2Token;
import com.leesh.quiz.global.constant.GrantType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NaverOauth2ApiService implements Oauth2ApiService {

    private final NaverAuthClient authClient;
    private final NaverApiClient apiClient;

    @Value("${oauth2.naver.client-id}")
    private String clientId;

    @Value("${oauth2.naver.client-secret}")
    private String clientSecret;

    @Value("${oauth2.naver.redirect-uri}")
    private String redirectUri;

    @Override
    public Oauth2Token requestToken(String authorizationCode) {

        NaverOauth2Token.Request request = NaverOauth2Token.Request.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type("authorization_code")
                .code(authorizationCode)
                .state("STATE")
                .build();

        return authClient.requestToken(MediaType.APPLICATION_FORM_URLENCODED_VALUE, request);

    }

    @Override
    public Oauth2Attributes requestUserInfo(String accessToken) {

        return apiClient.getUserInfo(MediaType.APPLICATION_JSON_VALUE, GrantType.BEARER.getType() + " " + accessToken);
    }
}
