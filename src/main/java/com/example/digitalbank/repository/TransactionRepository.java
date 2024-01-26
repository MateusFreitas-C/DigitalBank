package com.example.digitalbank.repository;

import com.example.digitalbank.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transactions, Integer> {
}
