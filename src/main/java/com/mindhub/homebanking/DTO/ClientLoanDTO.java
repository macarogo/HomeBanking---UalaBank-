package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long id;
    private long loanId;
    private double Amount;
    private int Payments;
    private  String name;

    public ClientLoanDTO(){}

    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.Amount = clientLoan.getAmount();
        this.Payments = clientLoan.getPayments();
        this.name = clientLoan.getLoan().getName();
    }

    public long getId() {
        return id;
    }
    public long getLoanId() {
        return loanId;
    }
    public double getAmount() {
        return Amount;
    }
    public int getPayments() {
        return Payments;
    }
    public String getName() {
        return name;
    }
}