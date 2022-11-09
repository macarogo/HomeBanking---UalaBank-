package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.Transactiontype;

import java.time.LocalDate;

public class TransactionDTO {
    private long id;
    private Transactiontype type;
    private double amount;
    private String description;
    private LocalDate date;
    private double balance;

    public TransactionDTO(){}

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.balance = transaction.getBalance();
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
    public double getBalance() {
        return balance;
    }
}