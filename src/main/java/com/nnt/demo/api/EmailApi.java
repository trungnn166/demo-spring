package com.nnt.demo.api;

import com.nnt.demo.response.Response;
import com.nnt.demo.services.UserService;
import com.nnt.demo.services.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RestController
@RequestMapping("/email")
public class EmailApi {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @GetMapping("/reset-password")
    public ResponseEntity<Response> requestResetPassword(@NotBlank @RequestParam("email") String email) {
        Optional user = userService.findByEmail(email);
//        if(!user.isPresent()) {
//            return ResponseEntity.ok(Response.ofError(HttpStatus.NOT_FOUND.value(), "Email not exits"));
//        }
        emailService.sendMailResetPassword(email);
        return ResponseEntity.ok(Response.ofNoContent());
    }
}
