package com.leesh.quiz.web.kakaotoken.client;


import com.leesh.quiz.web.kakaotoken.KaKaoTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://kauth.kakao.com", name = "kakaoTokenClient")
public interface KakaoTokenClient {

    @PostMapping(value = "/oauth/token", consumes = "application/json")
    KaKaoTokenDto.Response requestKakaoToken(@RequestHeader("Content-Type") String contentType,
                                    @SpringQueryMap KaKaoTokenDto.Request request
    );

}
