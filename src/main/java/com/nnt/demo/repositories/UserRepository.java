package com.nnt.demo.repositories;

import com.nnt.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("Update User as u Set u.password =:password "
            +"Where u.email = (Select rp.email From ResetPassword as rp Where rp.token =:token And rp.deletedAt IS NULL)")
    void updatePasswordByTokenResetPassword(String password, String token);

}
