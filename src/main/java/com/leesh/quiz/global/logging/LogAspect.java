package com.leesh.quiz.global.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("within(com.leesh.quiz..*)")
    private void allMethod() {
    }

    @Pointcut("within(com.leesh.quiz.global..*)")
    private void global() {
    }

    @Around("allMethod() && !global()")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        long start = System.currentTimeMillis();
        log.info("[Request] URI : {}, Method : {} , Parameters = {}", request.getRequestURI(), pjp.getSignature().toShortString(), pjp.getArgs());

        Object result = pjp.proceed();

        long end = System.currentTimeMillis();
        log.info("[Response] URI : {}, Method : {} , Results = {} , elapsed = {}", request.getRequestURI(), pjp.getSignature().toShortString(), result, end - start);

        return result;

    }

}
