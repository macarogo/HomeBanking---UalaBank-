package com.mindhub.homebanking.DTO;

public class LoanApplicationDto {

    private  long id;

    private Double amount;

    private int payments;

    private  String accountDestination;


    public LoanApplicationDto() {
    }

    public LoanApplicationDto(long id, Double amount, int payments, String accountDestination) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.accountDestination = accountDestination;
    }


    public Double getAmount() {
        return amount;
    }

    public long getId() {
        return id;
    }

    public int getPayments() {
        return payments;
    }

    public String getAccountDestination() {
        return accountDestination;
    }
}
