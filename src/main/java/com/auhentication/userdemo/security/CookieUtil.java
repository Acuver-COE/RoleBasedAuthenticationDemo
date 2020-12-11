package com.auhentication.userdemo.security;

import io.netty.handler.codec.http.cookie.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;

import java.time.Duration;

//@Component
public class CookieUtil {
    private static final String jwtTokenCookieName = "JWT-TOKEN";
    public static void create(ServerWebExchange serverWebExchange, WebSession webSession,
                              Model model,String emailId) {

        Integer hits = webSession.getAttribute ( "hits" );
        if (hits == null)
            hits = 0;
        webSession.getAttributes ().put ( "hits", ++hits );
        webSession.setMaxIdleTime ( Duration.ofSeconds ( 60 ) );
        webSession.getAttributes ().put ( FindByIndexNameSessionRepository
                .PRINCIPAL_NAME_INDEX_NAME, emailId );
        System.out.println ( "Session info"+ webSession.getId () );

    }
}
