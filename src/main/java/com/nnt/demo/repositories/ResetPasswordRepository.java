package com.nnt.demo.repositories;

import com.nnt.demo.entities.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {
    Boolean existsByEmail(String email);
    Optional<ResetPassword> findByEmail(String email);
}
