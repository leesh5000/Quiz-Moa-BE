package com.leesh.quiz.service;

import com.leesh.quiz.web.dto.AuthenticateDto;
import com.leesh.quiz.web.dto.RegisterDto;

/**
 * 사용자 인증 및 권한에 관한 비즈니스 로직을 담당하는 서비스
 * <p>
 *     사용자 인증 방법은 구현 기술에 따라 달라질 수 있기 때문에, 특정 기술에 종속되지 않기 위해 추상화 하였습니다.
 *     현재 구현체는 토큰 인증 방법을 사용하는 {@link TokenAuthenticationService} 입니다.
 * </p>
 *
 */
public interface AuthenticationService {

    void register(RegisterDto.Request request);

    AuthenticateDto.Response authenticate(AuthenticateDto.Request request);

}
