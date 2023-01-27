package com.leesh.quiz.global.configuration;

import com.leesh.quiz.global.jwt.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.leesh.quiz.global.util.RequestMatchersUtils.permitAllRequestMatchers;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 세션 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // CORS 설정
                .cors()
                .and()

                // 세션이 없으므로, CSRF 설정을 disable
                .csrf()
                .disable()

                // TODO H2 Database를 위한 설정으로 개발 완료 후 삭제하기
                .headers()
                .frameOptions()
                .disable()
                .and()

                // 예외 Entry Point 설정
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and()

                // 유저 인증일 위한 객체 설정
                .authenticationProvider(authenticationProvider())

                // JWT Filter 설정 : Spring Security Filter 보다 이전에 실행되기 위해 여기서 등록
                // Spring Security Filter에 등록되기 위해 빈 주입이 아닌 생성자 호출로 등록
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // 인증이 필요 없는 요청 설정
                .authorizeHttpRequests()
                .requestMatchers(permitAllRequestMatchers)
                .permitAll()

                // 그 외 모두 인증 필요
                .anyRequest()
                .authenticated()
        ;

        return http.build();
    }

    // 현재는 소셜 로그인만 지원하므로, 세부 내용을 구현하지 않은 상태임
    // 추후 별도로 회원가입, 로그인을 구현하게 되면 이 부분을 구현해야 함
    @Bean
    public UserDetailsService userDetailsService() {
        return (email) -> null;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, exception) -> {
            log.error("AuthenticationEntryPoint Exception Occur", exception);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, exception) -> {
            log.error("AccessDeniedHandler Exception Occur", exception);
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        };
    }

}
