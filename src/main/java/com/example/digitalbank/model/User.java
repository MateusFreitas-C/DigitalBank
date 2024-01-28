package com.example.digitalbank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Builder
@Getter @Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name may not be empty")
    private String name;

    @NotBlank(message = "Cpf may not be empty")
    private String cpf;

    @NotBlank(message = "Email may not be empty")
    @Email
    private String email;

    @NotBlank(message = "Password may not be empty")
    @Size(min = 6, message = "The password must be at least 6 characters")
    private String password;

    private BigDecimal balance;

    private BigDecimal creditLimit;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return cpf;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasSufficientBalance(BigDecimal transactionAmount) {
        return balance != null && balance.compareTo(transactionAmount) >= 0;
    }

    public boolean hasSufficientCreditLimit(BigDecimal transactionAmount) {
        return creditLimit != null && creditLimit.compareTo(transactionAmount) >= 0;
    }

    public void subtractBalance(BigDecimal amount){
        balance = balance.subtract(amount);
    }

    public void addBalance(BigDecimal amount){
        balance = balance.add(amount);
    }

    public void subtractCreditLimit(BigDecimal amount){
        creditLimit = creditLimit.subtract(amount);
    }

    public void addCreditLimit(BigDecimal amount){
        creditLimit = creditLimit.add(amount);
    }
}
