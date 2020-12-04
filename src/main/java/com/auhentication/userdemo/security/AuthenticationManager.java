package com.auhentication.userdemo.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private JwtUtil jwtUtil;

    AuthenticationManager(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        try {
            String username = jwtUtil.getUsernameFromToken(authToken);
            if (!jwtUtil.validateToken(authToken)) {
                return Mono.empty();
            }
            Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
            List<String> rolesMap = claims.get("role", List.class);
            List<GrantedAuthority> authorities = new ArrayList<> ();
            Iterator iterator = rolesMap.iterator();
            while(iterator.hasNext()) {
                System.out.println("roles"+iterator.next());
            }


            for ( String rolemap : rolesMap) {
                authorities.add(new SimpleGrantedAuthority (rolemap));
            }
            return Mono.just(new UsernamePasswordAuthenticationToken (username, null, authorities));
        } catch (Exception e) {
            return Mono.error ( new Exception ( "401 Unauthorized" ) );
        }
    }
}
