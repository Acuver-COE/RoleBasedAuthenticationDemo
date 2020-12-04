package com.auhentication.userdemo.service;

import com.auhentication.userdemo.exception.ConflictException;
import com.auhentication.userdemo.model.Role;
import com.auhentication.userdemo.model.Rolevalues;
import com.auhentication.userdemo.model.User;
import com.auhentication.userdemo.payload.ApiResponse;
import com.auhentication.userdemo.payload.AuthResponse;
import com.auhentication.userdemo.payload.LoginRequest;
import com.auhentication.userdemo.payload.SignUpRequest;
import com.auhentication.userdemo.repository.RoleRepository;
import com.auhentication.userdemo.repository.UserRepository;
import com.auhentication.userdemo.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Collections;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private JwtUtil jwtUtil;

    UserServiceImpl(UserRepository userRepository,RoleRepository roleRepository,
                JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
    }

    public Mono<User> createUser(SignUpRequest request) {
        return roleRepository.findByName ( Rolevalues.ROLE_REVIEWER)
                .map(role -> role)
                .flatMap(r->fetchTheUser(r,request));
    }

    private Mono<User> fetchTheUser(Role r, SignUpRequest req) {

        return userRepository.findByEmailId ( req.getEmailId ())
                .defaultIfEmpty ( new User ( null, req.getName (),
                        req.getEmailId (), req.getPassword (), req.getConfirmPassword (),
                        Collections.singletonList ( r ) ) )
                .flatMap ( r1->{
                    if(!req.getPassword ().equals ( req.getConfirmPassword () ))
                        throw new ConflictException ( "Password doesn't match"  ) ;
                    else {
                        if (r1.getAcuverId () == null) {
                            r1.setAcuverId ( req.getAcuverId () );
                            return userRepository.save ( r1 );
                        } else
                            throw new ConflictException ( "User Exist" );
                    }
                } );

    }

    public Mono<ResponseEntity<?>> findUserByUserName(LoginRequest request){
        return userRepository.findByEmailId ( request.getEmailId () )
                .map((userDetails) -> {
                    if (request.getPassword().equals(userDetails.getPassword())) {
                        return ResponseEntity.ok ( new AuthResponse (jwtUtil.generateToken(userDetails)));
                    } else {
                        return ResponseEntity.badRequest ()
                                .body ( new ApiResponse ( HttpStatus.BAD_REQUEST.value (),"Invalid credentials" ));
                    }
                }).defaultIfEmpty(ResponseEntity.badRequest ()
                        .body ( new ApiResponse ( HttpStatus.BAD_REQUEST.value (),"User does not exist" ) ));
    }

    public Mono<String> getMessage()
    {
        return Mono.just ("Message is for Reviewer");
    }

    public Mono<String> getMessage1()
    {
        return Mono.just ("Message is for Admin");
    }

    public Mono<String> getMessage2()
    {
        return Mono.just ("Message is for Ideator");
    }
}
