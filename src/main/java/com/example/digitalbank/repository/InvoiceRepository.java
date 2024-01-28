package com.example.digitalbank.repository;

import com.example.digitalbank.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    List<Invoice> findByUserId(Integer userId);

    List<Invoice> findByPaidAndUserId(boolean paid, Integer userId);
}
