package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanDto {

    private long id;

    private String name;

    private long maxAmount;

    private List<Integer> payments = new ArrayList<>();

    private int interest;


    public LoanDto() {
    }


    public LoanDto(Loan loan) {
        this.name = loan.getName();
        this.id = loan.getId();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.interest = loan.getInterest();
    }


    public long getId() { return id;}

    public String getName() {
        return name;
    }

    public long getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public int getInterest() {
        return interest;
    }

}
