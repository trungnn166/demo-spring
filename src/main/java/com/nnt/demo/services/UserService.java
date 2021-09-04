package com.nnt.demo.services;

import com.nnt.demo.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends BaseService<User>, UserDetailsService {
    Optional<User> findByEmail(String email);
}
