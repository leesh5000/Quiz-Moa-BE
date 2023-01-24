package com.leesh.quiz.external.oauth2.naver.service;

import com.leesh.quiz.external.oauth2.Oauth2ApiService;
import com.leesh.quiz.external.oauth2.Oauth2Attributes;
import com.leesh.quiz.external.oauth2.Oauth2Token;
import com.leesh.quiz.external.oauth2.naver.client.NaverApiClient;
import com.leesh.quiz.external.oauth2.naver.client.NaverAuthClient;
import com.leesh.quiz.external.oauth2.naver.dto.NaverOauth2TokenDto;
import com.leesh.quiz.global.jwt.constant.GrantType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class NaverOauth2ApiService implements Oauth2ApiService {

    private final NaverAuthClient authClient;
    private final NaverApiClient apiClient;

    private final String clientId;

    private final String clientSecret;

    private final String redirectUri;

    public NaverOauth2ApiService(NaverAuthClient authClient, NaverApiClient apiClient,
                                 @Value("${oauth2.naver.client-id}") String clientId,
                                 @Value("${oauth2.naver.client-secret}") String clientSecret,
                                 @Value("${oauth2.naver.redirect-uri}") String redirectUri) {
        this.authClient = authClient;
        this.apiClient = apiClient;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    @Override
    public Oauth2Token requestToken(String authorizationCode) {

        NaverOauth2TokenDto.Request request = NaverOauth2TokenDto.Request.builder()
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
