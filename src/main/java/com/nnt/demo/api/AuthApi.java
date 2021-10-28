package com.nnt.demo.api;

import com.nnt.demo.common.MethodUtils;
import com.nnt.demo.entities.User;
import com.nnt.demo.model.JwtResponse;
import com.nnt.demo.request.AuthRequest;
import com.nnt.demo.request.ResetPasswordRequest;
import com.nnt.demo.response.Response;
import com.nnt.demo.services.ResetPasswordService;
import com.nnt.demo.services.UserService;
import com.nnt.demo.services.impl.EmailService;
import com.nnt.demo.services.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthApi {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ResetPasswordService resetPasswordService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findByEmail(loginRequest.getEmail()).get();
        return ResponseEntity.ok(new JwtResponse(token, currentUser, userDetails.getAuthorities()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userService.findByEmail(email);
        return ResponseEntity.ok(user);

    }

    @GetMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.ok(Response.ofNoContent());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Response> requestResetPassword(@RequestBody AuthRequest authRequest) {
        Optional user = userService.findByEmail(authRequest.getEmail());
        if(user.isEmpty()) {
            return ResponseEntity.ok(Response.ofError(HttpStatus.NOT_FOUND.value(), "Email not exits"));
        }
        emailService.sendMailResetPassword(authRequest.getEmail());
        return ResponseEntity.ok(Response.ofNoContent());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Response> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok(Response.ofError(HttpStatus.NOT_FOUND.value(), "Change password error"));
        }

        if(!resetPasswordService.existsByToken(resetPasswordRequest.getToken())) {
            return ResponseEntity.ok(Response.ofError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Token not exits"));
        }

        userService.updatePasswordByTokenResetPassword( resetPasswordRequest.getNewPassword(),
                                                        resetPasswordRequest.getToken() );
        return ResponseEntity.ok(Response.ofNoContent());
    }

    @GetMapping("/is-token-reset-password")
    public ResponseEntity<Response> isTokenResetPassword(@RequestParam("token") String token) {
        if(MethodUtils.isEmpty(token) || !resetPasswordService.existsByToken(token)) {
            return ResponseEntity.ok(Response.ofError(HttpStatus.NOT_FOUND.value(), "Token not exits"));
        }
        return ResponseEntity.ok(Response.ofNoContent());
    }

}
