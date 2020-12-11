package com.auhentication.userdemo.service;

import com.auhentication.userdemo.exception.ConflictException;
import com.auhentication.userdemo.model.Role;
import com.auhentication.userdemo.model.Rolevalues;
import com.auhentication.userdemo.model.User;
import com.auhentication.userdemo.payload.*;
import com.auhentication.userdemo.repository.RoleRepository;
import com.auhentication.userdemo.repository.TokenRepository;
import com.auhentication.userdemo.repository.UserRepository;
import com.auhentication.userdemo.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private TokenRepository tokenRepository;
    private JwtUtil jwtUtil;


    UserServiceImpl(UserRepository userRepository,RoleRepository roleRepository,
                JwtUtil jwtUtil,TokenRepository tokenRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
    }

    public Mono<User> createUser(SignUpRequest request) {
        return roleRepository.findByName ( Rolevalues.ROLE_REVIEWER)
                .map(role -> role)
                .flatMap(r->fetchTheUser(r,request));
    }

    private Mono<User> fetchTheUser(Role r, SignUpRequest req) {

        return userRepository.findByEmailId ( req.getEmailId ())
                .defaultIfEmpty ( new User ( null,req.getAcuverId (), req.getName (),
                        req.getEmailId (), req.getPassword (), req.getConfirmPassword (),
                        Collections.singletonList ( r ), req.getTenantId ()) )
                .flatMap ( r1->{
                    if(!req.getPassword ().equals ( req.getConfirmPassword () ))
                        throw new ConflictException ( "Password doesn't match"  ) ;
                    else {
                        if (r1.getId () == null) {
                            if(req.getRoles ().size ()!=0)
                                r1.setRoles ( req.getRoles () );
                            return userRepository.save ( r1 );
                        } else
                            throw new ConflictException ( "User Exist" );
                    }
                } );

    }


    public Mono<ResponseEntity<?>> findUserByUserName1(LoginRequest request, WebSession webSession,
                                                      Model model,ServerWebExchange serverWebExchange) {

         return userRepository.findByEmailId ( request.getEmailId () )
                .map ( (userDetails) -> {
                    if(webSession.getAttributes ().get ( FindByIndexNameSessionRepository
                            .PRINCIPAL_NAME_INDEX_NAME)== null) {

                        if (request.getPassword ().equals ( userDetails.getPassword () )) {
                            Mono<TokenClass> t = searchForToken ( userDetails, request, webSession );
                            return ResponseEntity.ok ( t.map ( t1 -> t1 ) );
                        } else {
                            return ResponseEntity.badRequest ()
                                    .body ( new ApiResponse ( HttpStatus.BAD_REQUEST.value (), "Invalid credentials" ) );
                        }
                    }
                    else
                        throw new ConflictException ( "Other User is in session, logout and login again" );
                } ).defaultIfEmpty ( ResponseEntity.badRequest ()
                        .body ( new ApiResponse ( HttpStatus.BAD_REQUEST.value (), "User does not exist" ) ) );
    }

    private Mono<TokenClass> searchForToken(User userDetails, LoginRequest request, WebSession webSession) {

        return tokenRepository.findById ( request.getEmailId () )
                .defaultIfEmpty ( new TokenClass (null,null,null))
                .map ( t -> {
                    if(t.getUserName ()!=null) {
                        if (jwtUtil.isTokenExpired ( t.getToken () ))
                            return new TokenClass ( jwtUtil.generateToken ( userDetails, webSession )
                                    ,userDetails.getEmailId (),t.getExpireDate () );
                        else{
                            Integer hits = webSession.getAttribute ( "hits" );
                            if (hits == null)
                                hits = 0;
                            webSession.getAttributes ().put ( "hits", ++hits );
                            webSession.setMaxIdleTime ( Duration.ofSeconds ( 60 ) );
                            webSession.getAttributes ().put ( FindByIndexNameSessionRepository
                                    .PRINCIPAL_NAME_INDEX_NAME, request.getEmailId () );
                            System.out.println ( "Session info"+ webSession.getId () );
                            return new TokenClass ( t.getToken (),userDetails.getEmailId (),t.getExpireDate () );
                        }

                    }
                    else
                        return new TokenClass ( jwtUtil.generateToken ( userDetails, webSession )
                                ,userDetails.getEmailId (),t.getExpireDate () );
        } );

    }


    public Mono<String> getMessage(WebSession session)
    {
        if(session.getAttributes ().get ( FindByIndexNameSessionRepository
                .PRINCIPAL_NAME_INDEX_NAME)!= null )
            return Mono.just ("Message is for Reviewer");
        else
            return Mono.just("Session is expired, login again");
    }

    public Mono<String> getMessage1(WebSession session)
    {
        if(session.getAttributes ().get ( FindByIndexNameSessionRepository
                .PRINCIPAL_NAME_INDEX_NAME)!= null )
            return Mono.just("Message is for Admin");
        else
            return Mono.just("Session is expired, login again");
    }

    @Override
    public Mono<String> getMessage2(WebSession session) {
        if(session.getAttributes ().get ( FindByIndexNameSessionRepository
                .PRINCIPAL_NAME_INDEX_NAME)!= null )
            return Mono.just("Message is for AdminandIdeator");
        else
            return Mono.just("Session is expired, login again");
    }

    public Mono<String> getMessage3(WebSession session)
    {
        if(session.getAttributes ().get ( FindByIndexNameSessionRepository
                .PRINCIPAL_NAME_INDEX_NAME)!= null )
            return Mono.just("Message is for Admin");
        else
            return Mono.just("Session is expired, login again");
    }


    @Override
    public Mono<Void> sessionInvalidate(WebSession session) {
        String value = session.getAttributes ().get ( FindByIndexNameSessionRepository
                .PRINCIPAL_NAME_INDEX_NAME ).toString ();
         tokenRepository.findById ( value )
                .map ( t -> {
                    if (jwtUtil.isTokenExpired ( t.getToken () )) {
                        return tokenRepository.deleteById ( value );
                    } else
                        return t;
                } );

        return session.invalidate ();
    }


    public Mono<Void> deleteUser(String id) {
        return userRepository.deleteById ( id ).then ();
    }

    @Override
    public Mono<User> updateUser(String id, User user) {
        return userRepository.save(user);
    }

}
