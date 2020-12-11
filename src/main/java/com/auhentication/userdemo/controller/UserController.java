package com.auhentication.userdemo.controller;

import com.auhentication.userdemo.model.User;
import com.auhentication.userdemo.payload.LoginRequest;
import com.auhentication.userdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;
    UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/delete")
    @PreAuthorize( "hasRole('ADMIN')" )
    public Mono<Void> deleteUser(@Valid @PathVariable("id") String id){
        return userService.deleteUser ( id );
    }

    @PostMapping("/edit")
    @PreAuthorize( "hasRole('IDEATOR') or hasRole('ADMIN') or hasRole('REVIEWER')" )
    public Mono<User> editUser(@Valid @PathVariable("id") String id,
                               @Valid @RequestBody User user){
        return userService.updateUser ( id,user );
    }

}
