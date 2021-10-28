package com.nnt.demo.services.impl;

import com.nnt.demo.common.MethodUtils;
import com.nnt.demo.entities.User;
import com.nnt.demo.model.UserPrincipal;
import com.nnt.demo.repositories.UserRepository;
import com.nnt.demo.services.ResetPasswordService;
import com.nnt.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetPasswordService resetPasswordService;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }



    @Override
    public void delete(Long id) {

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(email);
        }

        return UserPrincipal.create(user.get());
    }

    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException(id.toString())
        );

        return UserPrincipal.create(user);
    }

    @Override
    public void updatePasswordByTokenResetPassword(String password, String token) {
        resetPasswordService.delete(token);
        userRepository.updatePasswordByTokenResetPassword(MethodUtils.encode(password), token);
    }
}
