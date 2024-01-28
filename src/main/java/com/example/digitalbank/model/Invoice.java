package com.example.digitalbank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Table(name = "invoices")
@Entity
@Getter @Setter
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer invoiceNumber;

    private BigDecimal amount;

    @JoinColumn(name = "source_user_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<Transactions> transactions;

    private boolean paid;

    public Invoice(BigDecimal amount, User user, Integer invoiceNumber, Transactions transaction){
        this.amount = amount;
        this.user = user;
        this.invoiceNumber = invoiceNumber;
        transactions = new ArrayList<>();
        transactions.add(transaction);
        paid = false;
    }

    public Invoice() {

    }

    public void payInvoice(BigDecimal amount){
        this.amount = this.amount.subtract(amount);
    }

    public void addAmountToInvoice(BigDecimal amount){
        this.amount = this.amount.add(amount);
    }
}
