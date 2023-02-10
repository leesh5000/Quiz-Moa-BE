package com.leesh.quiz.api.login.service;

import com.leesh.quiz.api.login.dto.Oauth2LoginDto;
import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.constant.Oauth2Type;
import com.leesh.quiz.domain.user.service.UserService;
import com.leesh.quiz.external.oauth2.Oauth2ApiService;
import com.leesh.quiz.external.oauth2.Oauth2Attributes;
import com.leesh.quiz.external.oauth2.Oauth2Token;
import com.leesh.quiz.global.jwt.dto.AccessToken;
import com.leesh.quiz.global.jwt.dto.RefreshToken;
import com.leesh.quiz.global.jwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.leesh.quiz.external.oauth2.Oauth2ApiServiceFactory.getOauth2ApiService;
import static com.leesh.quiz.global.util.DateTimeUtils.convertToLocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
public class Oauth2LoginService {

    private final UserService userService;
    private final TokenService tokenService;

    public Oauth2LoginDto.Response oauth2Login(Oauth2LoginDto.Request request) {

        // 외부 oauth2 서버로 부터 사용자 정보를 가져온다.
        var userInfo = getUserInfoFromProvider(request);

        // 해당 이메일로 가입된 회원이 있는지 확인하고 없으면 신규 가입을 한다.
        User user = userService.findUserOrRegister(userInfo);

        // jwt 토큰을 생성한다.
        AccessToken accessToken = tokenService.createAccessToken(user.getId(), user.getRole());
        RefreshToken refreshToken = tokenService.createRefreshToken(user.getId());

        // 유저의 refresh token을 업데이트한다.
        user.updateRefreshToken(
                refreshToken.refreshToken(),
                convertToLocalDateTime(
                        new Date(refreshToken.refreshTokenExpiresIn())
                )
        );

        return Oauth2LoginDto.Response.from(accessToken, refreshToken,
                createUserProfile(user));
    }

    private Oauth2LoginDto.UserProfile createUserProfile(User user) {

        return Oauth2LoginDto.UserProfile.builder()
                .id(user.getId())
                .name(user.getUsername())
                .email(user.getEmail())
                .picture(user.getProfile())
                .build();
    }

    private Oauth2Attributes getUserInfoFromProvider(Oauth2LoginDto.Request request) {

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
