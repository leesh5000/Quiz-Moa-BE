package com.leesh.quiz.api.login;

import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.domain.user.constant.Oauth2Type;
import com.leesh.quiz.external.oauth2.Oauth2ApiService;
import com.leesh.quiz.external.oauth2.Oauth2Attributes;
import com.leesh.quiz.external.oauth2.Oauth2Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.leesh.quiz.external.oauth2.Oauth2ApiServiceFactory.getOauth2ApiService;

@RequiredArgsConstructor
@Service
public class Oauth2LoginService {

    private final UserRepository userRepository;

    public void oauth2Login(Oauth2LoginDto.Request request) {

        // 외부 oauth2 서버로 부터 사용자 정보를 가져온다.
        var userInfo = getUserInfoFromOauth2Server(request);

        // 이미 가입된 사용자인지 확인한다.
        Optional<User> optionalUser = userRepository.findByEmail(userInfo.getEmail());

        if (optionalUser.isPresent()) {

            // 이미 가입된 사용자이면, 토큰을 발급


        } else {

            // 가입된 사용자가 아니라면, 신규 회원 가입하기

        }


        System.out.println();

    }

    private Oauth2Attributes getUserInfoFromOauth2Server(Oauth2LoginDto.Request request) {

        // oauth2 provider 타입을 가져온다.
        Oauth2Type oauth2Type = Oauth2Type.from(request.oauth2Type());

        // oauth2 provider 타입에 맞는 Oauth2ApiService를 가져온다.
        Oauth2ApiService oauth2ApiService = getOauth2ApiService(oauth2Type);

        // oauth2 인증 서버에 호출하여 인증 토큰을 가져온다.
        Oauth2Token oauth2Token = oauth2ApiService.requestToken(request.authorizationCode());

        // oauth2 API 서버에 호출하여 사용자 정보를 가져온다.
        return oauth2ApiService.requestUserInfo(oauth2Token.getAccessToken());
    }


}
