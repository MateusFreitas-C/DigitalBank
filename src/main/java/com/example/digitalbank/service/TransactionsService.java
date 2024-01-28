package com.example.digitalbank.service;

import com.example.digitalbank.dao.request.TransactionRequest;
import com.example.digitalbank.model.TransactionType;
import com.example.digitalbank.model.Transactions;
import com.example.digitalbank.model.User;

import java.math.BigDecimal;

public interface TransactionsService {
    Transactions debitTransaction(String destination, BigDecimal amount, String description);

    Transactions creditTransaction(String destination, BigDecimal amount, String description, Integer installmentNumber);

    void saveTransaction(TransactionRequest transaction);

    TransactionType verifyType(String type);

    User getTransactionSource();

    User getTransactionDestination(String destination);

    boolean isBalanceEnough(User source, BigDecimal amount);

    boolean isCreditLimitEnough(User source, BigDecimal amount);

    void subtractBalanceAmount(User user, BigDecimal amount);

    void addBalanceAmount(User user, BigDecimal amount);

    void subtractCreditLimitAmount(User user, BigDecimal amount);
}
