package com.nnt.demo.services.impl;

import com.nnt.demo.entities.ResetPassword;
import com.nnt.demo.repositories.ResetPasswordRepository;
import com.nnt.demo.services.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {
    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Override
    public Iterable<ResetPassword> findAll() {
        return null;
    }

    @Override
    public Optional<ResetPassword> findById(Long id) {
        return resetPasswordRepository.findById(id);
    }

    @Override
    public ResetPassword save(ResetPassword resetPassword) {
        return resetPasswordRepository.save(resetPassword);
    }

    @Override
    public ResetPassword update(ResetPassword resetPassword) {
        return resetPasswordRepository.save(resetPassword);
    }

    @Override
    public void delete(Long id) {
        ResetPassword resetPassword = findById(id).orElseGet(null);
        if(resetPassword == null) return;
        resetPassword.setDeletedAt(LocalDateTime.now());
        resetPassword.setIsExpired(true);
        update(resetPassword);
    }

    public ResetPassword create(String email, String token) {
        if(resetPasswordRepository.existsByEmail(email)) {
            ResetPassword resetPasswordCurrent = resetPasswordRepository.findByEmail(email).get();
            delete(resetPasswordCurrent.getId());
        }
        ResetPassword resetPassword = new ResetPassword(email, token);
        return save(resetPassword);
    }
}
