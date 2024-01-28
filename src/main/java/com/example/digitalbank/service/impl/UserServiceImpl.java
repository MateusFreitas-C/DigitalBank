package com.example.digitalbank.service.impl;

import com.example.digitalbank.model.User;
import com.example.digitalbank.repository.UserRepository;
import com.example.digitalbank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String cpf) {
                return userRepository.findByCpf(cpf)
                        .orElseThrow(() -> new UsernameNotFoundException("User not Found"));
            }
        };
    }

    @Override
    public Boolean existsByCpf(String cpf) {
        return userRepository.existsByCpf(cpf);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getByCpf(String cpf) {
        return userRepository.findByCpf(cpf).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User getById(Integer id) {
        return userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
