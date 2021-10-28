package com.nnt.demo.services;

import com.nnt.demo.entities.ResetPassword;

import java.util.Optional;

public interface ResetPasswordService extends BaseService<ResetPassword> {
    ResetPassword create(String email, String token);
    boolean existsByToken(String token);
    Optional<ResetPassword> findByToken(String token);
    void delete(String token);

}
