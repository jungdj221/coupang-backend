package com.kh.coupang.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성. 인증/인가 및 로그아웃 설정
    // filterchain은 버전마다 달라짐
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       return http
               .csrf(csrf -> csrf.disable())
               .httpBasic(basic -> basic.disable())
               .sessionManagement(session ->
                       session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               )
               .authorizeHttpRequests(authorize ->
                       authorize // 인증여부를 판단하는 로직 // 해당 location에 있는 거 접근 허용
                               .requestMatchers("/signUp").permitAll()
                               .anyRequest().authenticated()
               ).build();
        // 한번에 체인을 걸어서 해도 됨.
    }
}
