package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private Set<TransactionDTO> transactions = new HashSet<>();
    private AccountType type;

    public AccountDTO (){}

    public AccountDTO(Account account){
      this.id = account.getId();
      this.number = account.getNumber();
      this.creationDate = account.getCreationDate();
      this.balance = account.getBalance();
      this.transactions = account.getTransactions().stream().filter(transaction -> transaction.isActive()).map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
      this.type = account.getType();
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public long getId() {
        return id;
    }
    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
    public void setTransactions(Set<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
    public AccountType getType() {
        return type;
    }
}