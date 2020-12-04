package com.auhentication.userdemo.service;

import com.auhentication.userdemo.model.User;
import com.auhentication.userdemo.payload.LoginRequest;
import com.auhentication.userdemo.payload.SignUpRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface UserService {

    public abstract Mono<User> createUser(SignUpRequest request);

    public abstract Mono<ResponseEntity<?>> findUserByUserName(LoginRequest request);

    public abstract Mono<String> getMessage();

    public  abstract Mono<String> getMessage1();

    public abstract Mono<String> getMessage2();

}
