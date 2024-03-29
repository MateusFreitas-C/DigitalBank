package com.example.digitalbank.service;

import com.example.digitalbank.model.Invoice;
import com.example.digitalbank.model.Transactions;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceService {
    List<Invoice> getInvoicesByUser(Integer userId);

    BigDecimal calculateInstallmentAmount(BigDecimal amount, Integer installmentNumber);

    void createInvoice(BigDecimal amount, Integer installmentNumber, Integer userId, Transactions transaction);

    List<Invoice> findUserNonPaidInvoices(Integer userId);

    void payActualInvoice(Invoice invoice);

    Invoice getActualInvoice(Integer userId);
}
