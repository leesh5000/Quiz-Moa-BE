package com.leesh.quiz.web.kakaotoken;

import com.leesh.quiz.web.kakaotoken.client.KakaoTokenClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class KakaoTokenController {

    private final KakaoTokenClient client;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.client.redirect_uri}")
    private String redirectUri;

    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/oauth/kakao/callback")
    public @ResponseBody String loginCallback(String code) {

        String contentType = "application/x-www-form-urlencoded;charset=utf-8";

        KaKaoTokenDto.Request request = KaKaoTokenDto.Request.of(
                "authorization_code",
                clientId,
                redirectUri,
                code,
                clientSecret
        );

        System.out.println(request);
        KaKaoTokenDto.Response response = client.requestKakaoToken(contentType, request);

        return null;
    }

}
