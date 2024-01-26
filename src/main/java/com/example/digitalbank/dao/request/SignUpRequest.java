package com.example.digitalbank.dao.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter @Setter
public class SignUpRequest {
    private String name;

    private String cpf;

    private String email;

    private String password;
}
