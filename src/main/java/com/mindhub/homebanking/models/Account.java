package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
 public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch= FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    private boolean active;
    private AccountType type;

    public Account() {}

    public Account(Client client, String number, LocalDateTime creationDate, double balance, boolean active, AccountType type) {
        this.client = client;
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.active = active;
        this.type = type;
    }

    public long getId() {return id;}
    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}
    public LocalDateTime getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}
    public double getBalance() {return balance;}
    public void setBalance(double balance) {this.balance = balance;}

    @JsonIgnore
    public Client getClient() {return client;}

    public void setClient(Client client) {this.client = client;}

    @Override
    public String toString() {return super.toString();}

    public Set<Transaction> getTransactions() {return transactions;}

    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public AccountType getType() {
        return type;
    }
    public void setType(AccountType type) {
        this.type = type;
    }
}
