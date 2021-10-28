package com.nnt.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordRequest {
    private String token;
    private String currentPassword;
    @NotBlank
    private String newPassword;
    private String confirmPassword;
}
