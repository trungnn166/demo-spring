package com.nnt.demo.services.impl;

import com.nnt.demo.config.AppProperties;
import com.nnt.demo.entities.User;
import com.nnt.demo.model.UserPrincipal;
import com.nnt.demo.services.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Component
@Service
public class JwtService {
    private static final String SECRET_KEY = "c1G6AVSgxQfk8vmWbnhZ";
    private static final long EXPIRE_TIME = 86400L;

    @Autowired
    private UserService userService;

    @Autowired
    private AppProperties appProperties;

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + appProperties.getAuth().getTokenExpirationSec() * 1000))
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public String getEmailFromJwtToken(String token) {

        String email = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody().getSubject();
        return email;
    }

    public User getUserFromToken(String token) {

        String email = getEmailFromJwtToken(token);
        return userService.findByEmail(email).get();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
        }

        return false;
    }
}
