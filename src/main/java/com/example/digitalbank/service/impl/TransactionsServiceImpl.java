package com.example.digitalbank.service.impl;

import com.example.digitalbank.dao.request.TransactionRequest;
import com.example.digitalbank.exception.InsufficientBalanceException;
import com.example.digitalbank.exception.SelfTransactionException;
import com.example.digitalbank.model.Invoice;
import com.example.digitalbank.model.TransactionType;
import com.example.digitalbank.model.Transactions;
import com.example.digitalbank.model.User;
import com.example.digitalbank.repository.TransactionRepository;
import com.example.digitalbank.service.InvoiceService;
import com.example.digitalbank.service.TransactionsService;
import com.example.digitalbank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {
    private final UserService userService;

    private final TransactionRepository transactionRepository;

    private final InvoiceService invoiceService;

    @Override
    public Transactions debitTransaction(String destination, BigDecimal amount, String description, User source) {
        User userDestination = getTransactionDestination(destination);

        if(!isBalanceEnough(source, amount)) {
            throw new InsufficientBalanceException("balance");
        }

        subtractBalanceAmount(source, amount);
        addBalanceAmount(userDestination, amount);

        return Transactions.builder().source(source).destination(userDestination)
                .amount(amount).installmentNumber(0).description(description).type(TransactionType.DEBIT).timestamp(LocalDateTime.now()).build();
    }

    @Override
    public Transactions creditTransaction(String destination, BigDecimal amount, String description, Integer installmentNumber, User source) {
        User userDestination = getTransactionDestination(destination);

        if(!isCreditLimitEnough(source, amount)) {
            throw new InsufficientBalanceException("credit limit");
        }

        //source changes
        subtractCreditLimitAmount(source, amount);

        addBalanceAmount(userDestination, amount);

        return Transactions.builder().source(source).destination(userDestination)
                .amount(amount).installmentNumber(installmentNumber).description(description)
                .type(TransactionType.CREDIT).timestamp(LocalDateTime.now()).build();
    }

    @Override
    public void saveTransaction(TransactionRequest transaction) {
        TransactionType type = verifyType(transaction.getType());

        User source = getTransactionSource();
        validSourceAndDestination(source, transaction.getDestination());

        if(type == TransactionType.DEBIT){
            transactionRepository.save(debitTransaction(transaction.getDestination(), transaction.getAmount(),
                    transaction.getDescription(), source));

        }else if (type == TransactionType.CREDIT){
            Transactions transactions = transactionRepository.save(creditTransaction(transaction.getDestination(), transaction.getAmount(),
                    transaction.getDescription(), transaction.getInstallmentNumber(),source));

            invoiceService.createInvoice(transaction.getAmount(), transaction.getInstallmentNumber(), source.getId(), transactions);

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

    @Override
    public void validSourceAndDestination(User source, String destination) {
        if(source.getCpf().equals(destination)){
            throw new SelfTransactionException();
        }
    }

    @Override
    public void payActualInvoice(User user) {
        Invoice invoice = invoiceService.getActualInvoice(user.getId());

        if(!isBalanceEnough(user, invoice.getAmount())) {
            throw new InsufficientBalanceException("balance");
        }

        invoiceService.payActualInvoice(invoice);

        user.addCreditLimit(invoice.getAmount());
        subtractBalanceAmount(user, invoice.getAmount());

        userService.saveUser(user);

        transactionRepository.save(Transactions.builder().source(user).amount(invoice.getAmount()).installmentNumber(0)
                .description("Invoice Payment").type(TransactionType.DEBIT).timestamp(LocalDateTime.now()).build());
    }

    @Override
    public Page<Transactions> getTransactionsPageableByUserId(Integer userId, Integer pageSize, Integer pageNumber) {
        return transactionRepository.findAllBySourceId(userId, PageRequest.of(pageNumber, pageSize));
    }

}
