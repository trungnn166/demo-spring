package com.nnt.demo.services;

import com.nnt.demo.entities.ResetPassword;

public interface ResetPasswordService extends BaseService<ResetPassword> {
    public ResetPassword create(String email, String token);
}
