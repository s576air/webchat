package com.s576air.webchat.security;

import com.s576air.webchat.service.LoginDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final LoginDetailsService loginDetailsService;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    public SecurityConfig(
        LoginDetailsService loginDetailsService,
        CustomAuthenticationSuccessHandler authenticationSuccessHandler,
        CustomLogoutSuccessHandler logoutSuccessHandler
    ) {
        this.loginDetailsService = loginDetailsService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/login", "/sign-up", "/sign-up-success.html").permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(authenticationSuccessHandler)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"))
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginDetailsService)
            .passwordEncoder(passwordEncoder());
    }
}
