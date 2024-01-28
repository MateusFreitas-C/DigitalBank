package com.example.digitalbank.controller;

import com.example.digitalbank.dao.request.TransactionRequest;
import com.example.digitalbank.model.Transactions;
import com.example.digitalbank.model.User;
import com.example.digitalbank.service.TransactionsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionsService transactionsService;

    @PostMapping
    public ResponseEntity<String> saveTransaction(@RequestBody @Valid TransactionRequest request){
        transactionsService.saveTransaction(request);

        return ResponseEntity.status(HttpStatus.OK).body("Transaction Succedeed");
    }

    @GetMapping
    public ResponseEntity<Page<Transactions>> getTransactions(@RequestParam Integer pageSize, Integer pageNumber){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Transactions> response = transactionsService.getTransactionsPageableByUserId(user.getId(), pageSize, pageNumber);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/invoices/pay")
    public ResponseEntity<String> payActualInvoice(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        transactionsService.payActualInvoice(user);

        return ResponseEntity.status(HttpStatus.OK).body("Invoice paid successfully");
    }
}
