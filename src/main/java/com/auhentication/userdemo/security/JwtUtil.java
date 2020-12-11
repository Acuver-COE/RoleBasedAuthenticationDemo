package com.auhentication.userdemo.security;

import com.auhentication.userdemo.model.Rolevalues;
import com.auhentication.userdemo.model.User;
import com.auhentication.userdemo.payload.TokenClass;
import com.auhentication.userdemo.repository.TokenRepository;
import com.auhentication.userdemo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${springbootwebfluxjjwt.jjwt.secret}")
    private String secret;

    @Value("${springbootwebfluxjjwt.jjwt.expiration}")
    private String expirationTime;

    private Key key;

    private TokenRepository tokenRepository;
    private UserRepository userRepository;
    JwtUtil(TokenRepository tokenRepository, UserRepository userRepository){
        this.tokenRepository=tokenRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user, WebSession webSession) {
       /* Mono<String> res = tokenRepository.findByUserName ( user.getEmailId () )
                .map ( s -> {
                    System.out.println ( "expire----" + isTokenExpired ( s.getToken () ) );
                    System.out.println ( "expiretoken" + s.getToken () );
                    if (isTokenExpired ( s.getToken () )) {
                        return tokenGeneration ( user, webSession );
                    } else
                        return s.getToken ();
                } ).defaultIfEmpty (tokenGeneration ( user, webSession )  );

        return res;*/

        Map<String, Object> claims = new HashMap<> ();
        final List<Rolevalues> authorities =
                user.getRoles().stream ().map ( role -> role.getName () )
                        .collect ( Collectors.toList () );
        for(Rolevalues s: authorities){
            System.out.println ( "Roles11"+s.name () );

        }

        System.out.println ( authorities );
        claims.put("role", authorities );
        return doGenerateToken(claims, user.getEmailId (),webSession);
    }

    private String tokenGeneration(User user, WebSession webSession) {
        Map<String, Object> claims = new HashMap<> ();
        final List<Rolevalues> authorities =
                user.getRoles().stream ().map ( role -> role.getName () )
                        .collect ( Collectors.toList () );
        for(Rolevalues s: authorities){
            System.out.println ( "Roles11"+s.name () );
        }

        System.out.println ( authorities );
        claims.put("role", authorities );
        return doGenerateToken(claims, user.getEmailId (),webSession);
    }


    private String doGenerateToken(Map<String, Object> claims, String username, WebSession webSession) {
        Long expirationTimeLong = Long.parseLong(expirationTime); //in second

        final Date createdDate = new Date();
        final Date expirationDate = new Date (createdDate.getTime() + expirationTimeLong * 1000);

        Integer hits = webSession.getAttribute ( "hits" );
        if (hits == null)
            hits = 0;
        webSession.getAttributes ().put ( "hits", ++hits );
        webSession.setMaxIdleTime ( Duration.ofSeconds ( 60 ) );
        webSession.getAttributes ().put ( FindByIndexNameSessionRepository
                .PRINCIPAL_NAME_INDEX_NAME, username);
        System.out.println ( "Session info"+ webSession.getId () );

        String token = Jwts.builder ()
                .setClaims ( claims )
                .setSubject ( username )
                .setIssuedAt ( createdDate )
                .setExpiration ( expirationDate )
                .signWith ( key )
                .compact ();

         tokenRepository.save ( new TokenClass ( token, username,expirationDate ) )
                .subscribe ();


       return token;
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
