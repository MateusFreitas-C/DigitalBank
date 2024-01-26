package com.example.digitalbank.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name = "invoices")
@Getter @Setter
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer invoiceNumber;

    private BigDecimal amount;

    @JoinColumn(name = "source_user_id", referencedColumnName = "id")
    @ManyToOne
    private User user;

    private boolean paid;

    public Invoice(BigDecimal amount, User user, Integer invoiceNumber){
        this.amount = amount;
        this.user = user;
        this.invoiceNumber = invoiceNumber;
        paid = false;
    }

    public void payInvoice(BigDecimal amount){
        this.amount = this.amount.subtract(amount);
    }

    public void addAmountToInvoice(BigDecimal amount){
        this.amount = this.amount.add(amount);
    }
}
