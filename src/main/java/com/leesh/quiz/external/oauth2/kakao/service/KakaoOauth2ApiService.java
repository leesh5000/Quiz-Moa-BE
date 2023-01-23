package com.leesh.quiz.external.oauth2.kakao.service;

import com.leesh.quiz.external.oauth2.Oauth2ApiService;
import com.leesh.quiz.external.oauth2.Oauth2Attributes;
import com.leesh.quiz.external.oauth2.Oauth2Token;
import com.leesh.quiz.external.oauth2.kakao.client.KakaoApiClient;
import com.leesh.quiz.external.oauth2.kakao.client.KakaoAuthClient;
import com.leesh.quiz.external.oauth2.kakao.dto.KakaoOauth2Token;
import com.leesh.quiz.global.constant.GrantType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KakaoOauth2ApiService implements Oauth2ApiService {

    private final KakaoAuthClient authClient;

    private final KakaoApiClient apiClient;

    @Value("${oauth2.kakao.client-id}")
    private String clientId;

    @Value("${oauth2.kakao.client-secret}")
    private String clientSecret;

    @Value("${oauth2.kakao.redirect-uri}")
    private String redirectUri;

    @Override
    public Oauth2Token requestToken(String authorizationCode) {

        String contentType = "application/x-www-form-urlencoded;charset=utf-8";

        KakaoOauth2Token.Request request = KakaoOauth2Token.Request.builder()
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
