package com.leesh.quiz.global.resolver;

import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.BusinessException;
import com.leesh.quiz.global.jwt.service.TokenService;
import com.leesh.quiz.global.resolver.LoginUser.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private TokenService tokenService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean hasUserInfoClass = UserInfo.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginUserAnnotation && hasUserInfoClass;
    }

    /**
     * <p>
     *     인터셉터에서 저장된 유저 정보를 추출
     * </p>
     *
     * @see com.leesh.quiz.global.interceptor.AuthenticationInterceptor#preHandle
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        // 1. Request에서 유저 정보 추출
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Object userInfo = request.getAttribute(UserInfo.class.getName());

        // 2. 유저 정보가 없으면 예외 발생
        if (userInfo == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        return userInfo;
    }
}
