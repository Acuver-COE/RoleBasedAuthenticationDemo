package com.auhentication.userdemo.controller;

import com.auhentication.userdemo.model.User;
import com.auhentication.userdemo.payload.LoginRequest;
import com.auhentication.userdemo.payload.SignUpRequest;
import com.auhentication.userdemo.security.JwtUtil;
import com.auhentication.userdemo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.session.data.mongo.config.annotation.web.reactive.EnableMongoWebSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
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

    @PostMapping("/{tenantId}/login")
    public Mono<ResponseEntity<?>> loginUser(@Valid @PathVariable("tenantId") String tenantId,
                                             @Valid @RequestBody LoginRequest request,
                                             WebSession session, Model model, ServerWebExchange serverWebExchange){

        return userService.findUserByUserName1 (request,session,model,serverWebExchange);
    }

    @GetMapping("/{tenantId}/review")
    @PreAuthorize( "hasRole('REVIEWER')" )
    public Mono<String> getMessageForReviewer(WebSession session){
        return userService.getMessage (session);
    }

    @GetMapping("/{tenantId}/admin")
    @PreAuthorize( "hasRole('ADMIN')" )
    public Mono<String> getMessageForAdmin(WebSession session){
        return userService.getMessage1 (session);
    }

    @GetMapping("/{tenantId}/ideator")
    @PreAuthorize( "hasRole('IDEATOR')" )
    public Mono<String> getMessageForIdeator(WebSession session){
        return userService.getMessage2 (session);
    }

    @GetMapping("/{tenantId}/ideatorandadmin")
    @PreAuthorize( "hasRole('IDEATOR') or hasRole('ADMIN')" )
    public Mono<String> getMessageForIdeatorAndAdmin(WebSession session){
        return userService.getMessage3 (session);
    }

    @GetMapping("/{tenantId}/logout")
    public Mono<Void> logout(WebSession session){
        return userService.sessionInvalidate(session);
    }

}
