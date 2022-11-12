package com.mindhub.homebanking.services;

import com.mindhub.homebanking.DTO.ClientLoanDTO;
import com.mindhub.homebanking.DTO.LoanDto;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<ClientLoanDTO> getAllClientLoans();
    List<LoanDto> getAllLoans();
    Loan getLoan(Long id);
    void saveClientLoan(ClientLoan clientLoan);
    void saveLoan(Loan loan);
}