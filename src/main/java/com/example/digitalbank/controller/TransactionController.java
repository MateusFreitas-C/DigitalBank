package com.example.digitalbank.controller;

import com.example.digitalbank.dao.request.TransactionRequest;
import com.example.digitalbank.model.User;
import com.example.digitalbank.service.TransactionsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionsService transactionsService;

    @PostMapping
    public ResponseEntity<String> getSource(@RequestBody @Valid TransactionRequest request){
        transactionsService.saveTransaction(request);

        return ResponseEntity.status(HttpStatus.OK).body("Transaction Succedeed");
    }
}
