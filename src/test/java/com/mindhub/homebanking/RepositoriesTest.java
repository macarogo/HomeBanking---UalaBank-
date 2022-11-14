package com.mindhub.homebanking;

import com.mindhub.homebanking.Repository.*;
import com.mindhub.homebanking.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;



@SpringBootTest

@AutoConfigureTestDatabase(replace = NONE)

public class RepositoriesTest {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private LoanRepository loanRepository;

    @Test
    public void existClient(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients,is(not(empty())));
    }

    @Test
    public void existClientLorenzo(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("firstName", is("Lorenzo"))));
    }

    @Test
    public void existAccount(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,is(not(empty())));
    }

    @Test
    public void existAccountVIN001(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,hasItem(hasProperty("number", is("VIN-001"))));
    }

    @Test
    public void existCard(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards,is(not(empty())));
    }

    @Test
    public void existCardLorenzoMalbel(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("cardHolder", is("Lorenzo Malbel"))));
    }

    @Test
    public void existTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }

    @Test
    public void existPersonalTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("description", is("Pago por Electricidad"))));
    }

    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }
}