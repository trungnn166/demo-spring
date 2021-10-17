package com.nnt.demo.api;

import com.nnt.demo.config.AppProperties;
import com.nnt.demo.entities.User;
import com.nnt.demo.model.JwtResponse;
import com.nnt.demo.request.LoginRequest;
import com.nnt.demo.response.Response;
import com.nnt.demo.services.UserService;
import com.nnt.demo.services.impl.EmailService;
import com.nnt.demo.services.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private AppProperties appProperties;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
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

    @GetMapping("/reset-password")
    public ResponseEntity<Response> resetPassword(@RequestParam("email") String email) {
        return ResponseEntity.ok(Response.ofNoContent());
    }
}
