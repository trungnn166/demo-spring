package com.nnt.demo.entities;

import lombok.Data;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "reset_password")
@Where(clause = "deleted_at is null")
@Data
public class ResetPassword {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    @NotBlank
    @Email
    private String email;

    @Column(name = "token")
    @NotBlank
    @UniqueElements
    private String token;

    @Column(name = "is_expired")
    private Boolean isExpired;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
