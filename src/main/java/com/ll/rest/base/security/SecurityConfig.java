package com.ll.rest.base.security;

import com.ll.rest.base.security.entryPoint.ApiAuthenticationEntryPoint;
import com.ll.rest.base.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final ApiAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // 아래에 설정한 내용들이 /api/** 경로에만 적용된다.
                .exceptionHandling( //사용자 인증 실패 시, 예외 메시지 커스텀하기
                        exceptionHandling -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint)
                )
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(HttpMethod.POST, "/api/*/member/login").permitAll() // 로그인은 누구나 가능
                                .requestMatchers(HttpMethod.GET, "/api/*/articles").permitAll() // 글 전체조회는 누구나 가능
                                .requestMatchers(HttpMethod.GET, "/api/*/articles/*").permitAll() // 글 단건 조회는 누구나 가능
                                .anyRequest().authenticated()
                )
                .cors().disable() //타 도메인에서 API 호출 가능
                .csrf().disable() //CSRF 토큰 끄기
                .httpBasic().disable() // httpBasic 로그인 방식 끄기
                .formLogin().disable() // form 로그인 방식 끄기
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ) //세션 끄기
                //jwtAuthorizationFilter 를 UsernamePasswordAuthenticationFilter 앞에 둬서 먼저 동작하게 한다.
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
