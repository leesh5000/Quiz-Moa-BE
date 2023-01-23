package com.leesh.quiz.external.oauth2.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leesh.quiz.domain.user.constant.Oauth2Type;
import com.leesh.quiz.external.oauth2.Oauth2Attributes;
import org.springframework.util.StringUtils;

public record KakaoUserInfoResponse(String id,
                                    @JsonProperty("kakao_account") KakaoAccount kakaoAccount) implements Oauth2Attributes {

        public record KakaoAccount(String email, Profile profile) {

                public record Profile(String nickname,
                                      @JsonProperty("thumbnail_image_url") String thumbnailImageUrl) {

            }
        }

    @Override
    public String getName() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public String getEmail() {
        return !StringUtils.hasText(kakaoAccount.email) ?
                id : kakaoAccount.email;
    }

    @Override
    public String getProfile() {
        return kakaoAccount.profile.thumbnailImageUrl;
    }

    @Override
    public String getOauth2Type() {
        return Oauth2Type.KAKAO.name();
    }

}
