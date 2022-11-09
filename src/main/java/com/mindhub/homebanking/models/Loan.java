package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String name;
    private long maxAmount;

    @ElementCollection
    @Column(name="payments")
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy="loan", fetch= FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    private int interest;

    public Loan(){}

    public Loan(String name, long maxAmount, int interest,List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interest = interest;
    }

    public long getId() {return id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public long getMaxAmount() {return maxAmount;}
    public void setMaxAmount(long maxAmount) {this.maxAmount = maxAmount;}
    public List<Integer> getPayments() {return payments;}
    public void setPayments(List<Integer> payments) {this.payments = payments;}
    public Set<ClientLoan> getClientLoans() {return clientLoans;}
    public void addClientLoan(ClientLoan clientLoan) {clientLoan.setLoan(this);clientLoans.add(clientLoan);}
    public  List<Client> getClient(){return clientLoans.stream().map(clientLoan -> clientLoan.getClient()).collect(Collectors.toList());}
    public int getInterest() {
        return interest;
    }
    public void setInterest(int interest) {
        this.interest = interest;
    }
}
