package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.ClientLoanDTO;
import com.mindhub.homebanking.DTO.LoanDto;
import com.mindhub.homebanking.Repository.ClientLoanRepository;
import com.mindhub.homebanking.Repository.LoanRepository;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    LoanRepository loanRepository;

    @Override
    public List<ClientLoanDTO> getAllClientLoans() {
        return clientLoanRepository.findAll().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(toList());
    }

    @Override
    public List<LoanDto> getAllLoans() {
        return loanRepository.findAll().stream().map(loan -> new LoanDto(loan)).collect(Collectors.toList());
    }

    @Override
    public Loan getLoan(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }
}