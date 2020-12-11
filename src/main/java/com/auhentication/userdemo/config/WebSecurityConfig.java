package com.auhentication.userdemo.config;

import com.auhentication.userdemo.model.Rolevalues;
import com.auhentication.userdemo.security.AuthenticationManager;
import com.auhentication.userdemo.security.SecurityContextRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

    private AuthenticationManager authenticationManager;
    private SecurityContextRepository securityContextRepository;

    WebSecurityConfig(AuthenticationManager authenticationManager,
                      SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }
    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

            return http
                    .exceptionHandling()
                    .authenticationEntryPoint((swe, e) -> {
                        return Mono.fromRunnable(() -> {
                            swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        });
                    }).accessDeniedHandler((swe, e) -> {
                        return Mono.fromRunnable(() -> {
                            swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        });
                    }).and()
                    .csrf().disable()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .authenticationManager(authenticationManager)
                    .securityContextRepository(securityContextRepository)
                    .authorizeExchange()
                    .pathMatchers(HttpMethod.OPTIONS).permitAll()
                    .pathMatchers("/api/auth/{tenantId}/login").permitAll()
                    .pathMatchers ( "/api/auth/signup" ).permitAll ()
                    //.pathMatchers ( "/api/auth/{tenantId}/logout" ).permitAll ()
                    .anyExchange().authenticated()
                    .and().build();
        }
    }

