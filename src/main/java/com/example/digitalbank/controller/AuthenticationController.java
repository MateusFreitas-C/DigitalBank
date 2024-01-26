package com.example.digitalbank.controller;

import com.example.digitalbank.dao.request.SignUpRequest;
import com.example.digitalbank.dao.request.SigninRequest;
import com.example.digitalbank.dao.response.JwtAuthenticationResponse;
import com.example.digitalbank.security.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        log.debug("Sign-up executed by {}", request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        JwtAuthenticationResponse response = authenticationService.signin(request);

        log.debug("Sign-in executed by {}", request.getCpf());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
