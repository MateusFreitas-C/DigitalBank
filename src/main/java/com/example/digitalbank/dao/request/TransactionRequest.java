package com.example.digitalbank.dao.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Setter
public class TransactionRequest {
    @NotBlank(message = "Destination may not be empty")
    String destination;

    @NotNull(message = "Amount may not be null")
    BigDecimal amount;

    String description;

    @NotBlank(message = "Transaction type may not be empty")
    String type;

    Integer installmentNumber;
}
