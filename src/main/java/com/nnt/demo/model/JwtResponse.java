package com.nnt.demo.model;

import com.nnt.demo.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private User user;
    private Collection<? extends GrantedAuthority> roles;
}
