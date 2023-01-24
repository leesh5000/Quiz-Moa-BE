package com.leesh.quiz.external.oauth2.google.service;

import com.leesh.quiz.external.oauth2.Oauth2ApiService;
import com.leesh.quiz.external.oauth2.Oauth2Attributes;
import com.leesh.quiz.external.oauth2.Oauth2Token;
import com.leesh.quiz.external.oauth2.google.client.GoogleApiClient;
import com.leesh.quiz.external.oauth2.google.client.GoogleAuthClient;
import com.leesh.quiz.external.oauth2.google.dto.GoogleOauth2TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class GoogleOauth2ApiService implements Oauth2ApiService {

    private final GoogleAuthClient authClient;

    private final GoogleApiClient apiClient;

    private final String clientId;

    private final String clientSecret;

    private final String redirectUri;

    public GoogleOauth2ApiService(GoogleAuthClient authClient, GoogleApiClient apiClient,
                                  @Value("${oauth2.google.client-id}") String clientId,
                                  @Value("${oauth2.google.client-secret}") String clientSecret,
                                  @Value("${oauth2.google.redirect-uri}") String redirectUri) {
        this.authClient = authClient;
        this.apiClient = apiClient;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    @Override
    public Oauth2Token requestToken(String authorizationCode) {

        var request = GoogleOauth2TokenDto.Request.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type("authorization_code")
                .code(authorizationCode)
                .redirect_uri(redirectUri)
                .build();

        return authClient.requestToken(MediaType.APPLICATION_FORM_URLENCODED_VALUE, request);
    }

    @Override
    public Oauth2Attributes requestUserInfo(String accessToken) {

        return apiClient.getUserInfo(MediaType.APPLICATION_JSON_VALUE, accessToken);
    }
}
