package com.example.digitalbank.service.impl;

import com.example.digitalbank.exception.InvoiceNotFoundException;
import com.example.digitalbank.model.Invoice;

import com.example.digitalbank.repository.InvoiceRepository;
import com.example.digitalbank.service.InvoiceService;
import com.example.digitalbank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserService userService;
    @Override
    public List<Invoice> getInvoicesByUser(Integer userId) {
        return invoiceRepository.findByUserId(userId);
    }

    @Override
    public BigDecimal calculateInstallmentAmount(BigDecimal amount, Integer installmentNumber) {
        return amount.divide(BigDecimal.valueOf(installmentNumber));
    }

    @Override
    public void createInvoice(BigDecimal amount, Integer installmentNumber, Integer userId) {
        List<Invoice> invoices = findUserNonPaidInvoices(userId);
        BigDecimal installmentAmount = calculateInstallmentAmount(amount, installmentNumber);

        if (invoices.isEmpty()) {
            createInvoices(installmentAmount, installmentNumber, userId);
        } else if (invoices.size() >= installmentNumber) {
            updateExistingInvoices(invoices, installmentAmount, installmentNumber);
        } else {
            updateExistingInvoices(invoices, installmentAmount, invoices.size());
            createAdditionalInvoices(invoices.size() + 1, installmentNumber, installmentAmount, userId);
        }
    }

    private void createInvoices(BigDecimal installmentAmount, Integer installmentNumber, Integer userId) {
        for (int i = 1; i <= installmentNumber; i++) {
            Invoice invoice = new Invoice(installmentAmount, userService.getById(userId), i);
            invoiceRepository.save(invoice);
        }
    }

    private void updateExistingInvoices(List<Invoice> invoices, BigDecimal installmentAmount, Integer installmentNumber) {
        for (Invoice invoice : invoices.subList(0, installmentNumber)) {
            invoice.addAmountToInvoice(installmentAmount);
            invoiceRepository.save(invoice);
        }
    }

    private void createAdditionalInvoices(int startInvoiceNumber, int endInvoiceNumber, BigDecimal installmentAmount, Integer userId) {
        for (int i = startInvoiceNumber; i <= endInvoiceNumber; i++) {
            Invoice invoice = new Invoice(installmentAmount, userService.getById(userId), i);
            invoiceRepository.save(invoice);
        }
    }

    @Override
    public List<Invoice> findUserNonPaidInvoices(Integer userId) {
        return invoiceRepository.findByPaidAndUserId(false, userId);
    }

    @Override
    public void payActualInvoice(Invoice invoice) {
        invoice.setPaid(true);
        invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getActualInvoice(Integer userId) {
        List<Invoice> invoices = invoiceRepository.findByPaidAndUserId(false, userId);

        if(invoices.isEmpty()){
            throw new InvoiceNotFoundException("There is no invoice to be paid");
        }

        return invoices.get(0);
    }


}
