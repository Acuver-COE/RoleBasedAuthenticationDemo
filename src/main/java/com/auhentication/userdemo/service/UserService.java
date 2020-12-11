package com.auhentication.userdemo.service;

import com.auhentication.userdemo.model.User;
import com.auhentication.userdemo.payload.LoginRequest;
import com.auhentication.userdemo.payload.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.ui.Model;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

public interface UserService {

    public abstract Mono<User> createUser(SignUpRequest request);

    public abstract Mono<ResponseEntity<?>> findUserByUserName1(LoginRequest request,
                                                               WebSession session, Model model,
                                                               ServerWebExchange serverWebExchange);

    public abstract Mono<String> getMessage(WebSession session);

    public  abstract Mono<String> getMessage1(WebSession session);

    public abstract Mono<String> getMessage2(WebSession session);

    public abstract Mono<String> getMessage3(WebSession session);


    public abstract Mono<Void> sessionInvalidate(WebSession session);

    public abstract Mono<Void> deleteUser(String id);

    public abstract Mono<User> updateUser ( String id,User user );
}
