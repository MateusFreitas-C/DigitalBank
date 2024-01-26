package com.example.digitalbank.security.service;

import com.example.digitalbank.dao.request.SignUpRequest;
import com.example.digitalbank.dao.request.SigninRequest;
import com.example.digitalbank.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}
