package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Transactiontype type;
    private double amount;
    private String description;
    private LocalDate date;
    private boolean active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;

    private double balance;

    public Transaction() {}

    public Transaction(Transactiontype type, double amount, String description, LocalDate date, boolean active,double balance,Account account) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.account = account;
        this.active = active;
        this.balance = balance;
    }


    public long getId() {
        return id;
    }
    public Transactiontype getType() {
        return type;
    }
    public void setType(Transactiontype type) {
        this.type = type;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
}