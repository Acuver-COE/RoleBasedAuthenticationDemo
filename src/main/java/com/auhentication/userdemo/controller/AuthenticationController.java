package com.auhentication.userdemo.controller;

import com.auhentication.userdemo.model.User;
import com.auhentication.userdemo.payload.LoginRequest;
import com.auhentication.userdemo.payload.SignUpRequest;
import com.auhentication.userdemo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

private UserService userService;

    AuthenticationController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public Mono<User> registerUser(@Valid @RequestBody SignUpRequest request){
        return userService.createUser ( request );
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> loginUser(@Valid @RequestBody LoginRequest request){
        return userService.findUserByUserName (request);
    }

    @GetMapping("/review")
    @PreAuthorize( "hasRole('REVIEWER')" )
    public Mono<String> getMessageForReviewer(){
        return userService.getMessage ();
    }

    @GetMapping("/admin")
    @PreAuthorize( "hasRole('ADMIN')" )
    public Mono<String> getMessageForAdmin(){
        return userService.getMessage1 ();
    }

    @GetMapping("/ideator")
    @PreAuthorize( "hasRole('IDEATOR')" )
    public Mono<String> getMessageForIdeator(){
        return userService.getMessage2 ();
    }

}
