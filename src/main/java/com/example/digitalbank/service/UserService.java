package com.example.digitalbank.service;

import com.example.digitalbank.model.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();

    Boolean existsByCpf(String cpf);

    void saveUser(User user);

    User getByCpf(String cpf);
}
