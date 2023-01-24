package com.leesh.quiz.external.oauth2.kakao.service;

import com.leesh.quiz.external.oauth2.Oauth2ApiService;
import com.leesh.quiz.external.oauth2.Oauth2Attributes;
import com.leesh.quiz.external.oauth2.Oauth2Token;
import com.leesh.quiz.external.oauth2.kakao.client.KakaoApiClient;
import com.leesh.quiz.external.oauth2.kakao.client.KakaoAuthClient;
import com.leesh.quiz.external.oauth2.kakao.dto.KakaoOauth2TokenDto;
import com.leesh.quiz.global.jwt.constant.GrantType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoOauth2ApiService implements Oauth2ApiService {

    private final KakaoAuthClient authClient;

    private final KakaoApiClient apiClient;

    private final String clientId;

    private final String clientSecret;

    private final String redirectUri;

    public KakaoOauth2ApiService(KakaoAuthClient authClient,
                                 KakaoApiClient apiClient,
                                 @Value("${oauth2.kakao.client-id}") String clientId,
                                 @Value("${oauth2.kakao.client-secret}") String clientSecret,
                                 @Value("${oauth2.kakao.redirect-uri}") String redirectUri) {

        this.authClient = authClient;
        this.apiClient = apiClient;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    @Override
    public Oauth2Token requestToken(String authorizationCode) {

        String contentType = "application/x-www-form-urlencoded;charset=utf-8";

        KakaoOauth2TokenDto.Request request = KakaoOauth2TokenDto.Request.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type("authorization_code")
                .code(authorizationCode)
                .redirect_uri(redirectUri)
                .build();

        return authClient.requestToken(contentType, request);
    }

    @Override
    public Oauth2Attributes requestUserInfo(String accessToken) {

        String contentType = "application/x-www-form-urlencoded;charset=utf-8";

        return apiClient.getUserInfo(contentType, GrantType.BEARER.getType() + " " + accessToken);

    }

}
