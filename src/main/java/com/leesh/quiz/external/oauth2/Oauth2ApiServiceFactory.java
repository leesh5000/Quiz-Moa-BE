package com.leesh.quiz.external.oauth2;

import com.leesh.quiz.domain.user.constant.Oauth2Type;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Oauth2ApiServiceFactory {

    private static Map<String, Oauth2ApiService> oauth2ApiServices;

    private Oauth2ApiServiceFactory(Map<String, Oauth2ApiService> oauth2ApiServices) {
        Oauth2ApiServiceFactory.oauth2ApiServices = oauth2ApiServices;
    }

    public static Oauth2ApiService getOauth2ApiService(Oauth2Type type) {

        String oauth2ApiServiceBeanName = switch (type) {
            case KAKAO -> "kakaoOauth2ApiService";
            case NAVER -> "naverOauth2ApiService";
            case GOOGLE -> "googleOauth2ApiService";
        };

        if (!oauth2ApiServices.containsKey(oauth2ApiServiceBeanName)) {
            throw new IllegalArgumentException("Oauth2TokenClientFactory.getOauth2TokenClient() : " +
                    "oauth2ApiServiceBeanName is not exist. oauth2ApiServiceBeanName = " + oauth2ApiServiceBeanName);
        }

        return oauth2ApiServices.get(oauth2ApiServiceBeanName);
    }

}
