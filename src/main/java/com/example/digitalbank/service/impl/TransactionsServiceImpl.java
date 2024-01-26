package com.example.digitalbank.service.impl;

import com.example.digitalbank.dao.request.TransactionRequest;
import com.example.digitalbank.exception.InsufficientBalanceException;
import com.example.digitalbank.model.TransactionType;
import com.example.digitalbank.model.Transactions;
import com.example.digitalbank.model.User;
import com.example.digitalbank.repository.TransactionRepository;
import com.example.digitalbank.service.TransactionsService;
import com.example.digitalbank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {
    private final UserService userService;

    private final TransactionRepository transactionRepository;
    @Override
    public Transactions debitTransaction(String destination, BigDecimal amount, String description) {
        User source = getTransactionSource();
        User userDestination = getTransactionDestination(destination);

        if(!isBalanceEnough(source, amount)) {
            throw new InsufficientBalanceException("balance");
        }

        subtractBalanceAmount(source, amount);
        addBalanceAmount(userDestination, amount);

        return Transactions.builder().source(source).destination(userDestination)
                .amount(amount).description(description).type(TransactionType.DEBIT).timestamp(LocalDateTime.now()).build();
    }

    @Override
    public Transactions creditTransaction(String destination, BigDecimal amount, String description) {
        User source = getTransactionSource();
        User userDestination = getTransactionDestination(destination);

        if(!isCreditLimitEnough(source, amount)) {
            throw new InsufficientBalanceException("credit limit");
        }

        //source changes
        subtractCreditLimitAmount(source, amount);
        //TODO criar invoice service e repository e obter faturas nao pagas por usuario


        addBalanceAmount(userDestination, amount);

        return Transactions.builder().source(source).destination(userDestination)
                .amount(amount).description(description).type(TransactionType.CREDIT).timestamp(LocalDateTime.now()).build();
    }

    @Override
    public void saveTransaction(TransactionRequest transaction) {
        TransactionType type = verifyType(transaction.getType());

        if(type == TransactionType.DEBIT){
            transactionRepository.save(debitTransaction(transaction.getDestination(), transaction.getAmount(),
                    transaction.getDescription()));

        }else if (type == TransactionType.CREDIT){
            transactionRepository.save(creditTransaction(transaction.getDestination(), transaction.getAmount(),
                    transaction.getDescription()));

        }else{
            throw new IllegalArgumentException("Transaction type not available");
        }


    }

    @Override
    public TransactionType verifyType(String type) {
        return TransactionType.valueOf(type);
    }

    @Override
    public User getTransactionSource() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public User getTransactionDestination(String destination){
        return userService.getByCpf(destination);
    }

    @Override
    public boolean isBalanceEnough(User source, BigDecimal amount) {
        return source.hasSufficientBalance(amount);
    }

    @Override
    public boolean isCreditLimitEnough(User source, BigDecimal amount) {
        return source.hasSufficientCreditLimit(amount);
    }

    @Override
    public void subtractBalanceAmount(User user, BigDecimal amount) {
        user.subtractBalance(amount);
        userService.saveUser(user);
    }

    @Override
    public void addBalanceAmount(User user, BigDecimal amount) {
        user.addBalance(amount);
        userService.saveUser(user);
    }

    @Override
    public void subtractCreditLimitAmount(User user, BigDecimal amount) {
        user.subtractCreditLimit(amount);
        userService.saveUser(user);
    }

}
