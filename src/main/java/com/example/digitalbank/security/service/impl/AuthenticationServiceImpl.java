package com.example.digitalbank.security.service.impl;

import com.example.digitalbank.controller.AuthenticationController;
import com.example.digitalbank.dao.request.SignUpRequest;
import com.example.digitalbank.dao.request.SigninRequest;
import com.example.digitalbank.dao.response.JwtAuthenticationResponse;
import com.example.digitalbank.model.Role;
import com.example.digitalbank.model.User;
import com.example.digitalbank.repository.UserRepository;
import com.example.digitalbank.security.service.AuthenticationService;
import com.example.digitalbank.security.service.JwtService;
import com.example.digitalbank.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        if (userService.existsByCpf(request.getCpf())) throw new IllegalArgumentException("User already exists!");

        String password = passwordEncoder.encode(request.getPassword());

        User user = User.builder().name(request.getName()).cpf(request.getCpf()).email(request.getEmail())
                .password(password).balance(BigDecimal.valueOf(0)).creditLimit(BigDecimal.valueOf(800))
                .role(Role.USER).build();

        userService.saveUser(user);

        String token = jwtService.generateToken(user);

        return JwtAuthenticationResponse.builder().token(token).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        User user = userService.getByCpf(request.getCpf());

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
            new UsernamePasswordAuthenticationToken(request.getCpf(), request.getPassword());
        }else {
            throw new IllegalArgumentException("Invalid password");
        }

        String token = jwtService.generateToken(user);

        return JwtAuthenticationResponse.builder().token(token).build();
    }
}
