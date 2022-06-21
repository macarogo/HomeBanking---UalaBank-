package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.Repository.AccountRepository;
import com.mindhub.homebanking.Repository.ClientRepository;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AccountController {


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;


    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccount();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccounts(@PathVariable Long id){
        return accountService.getAccounts(id);
    }

    @Autowired
    private ClientRepository clientRepository;


    @PostMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> newAccount(Authentication authentication, @RequestParam AccountType type) {

    Client client = clientService.getclientCurrent(authentication);

    if (client.getAccount().size() >= 3){

        return  new ResponseEntity<>("You can't have sare than 3 accounts", HttpStatus.FORBIDDEN);
    }

    Account newAccount = new Account(client, accountService.accountValidate(), LocalDateTime.now(), 0, true, type);
    accountService.saveAccount(newAccount);
    return new ResponseEntity<>("Account created successfully" ,HttpStatus.CREATED);
    }


    @PatchMapping("/clients/current/accounts/{id}")
    public ResponseEntity<Object> ocultarAccount(@PathVariable Long id){
        Account accountActive = accountService.getAccountById(id);

        if (accountActive.getBalance() > 0){
            return  new ResponseEntity<>("Tu cuenta tiene saldo", HttpStatus.FORBIDDEN);
        }

        Set<Transaction> transactions = accountActive.getTransactions();
        transactions.forEach(transaction -> transaction.setActive(false));
        transactions.forEach(transaction -> transactionService.saveTransaction(transaction));

        accountActive.setActive(false);
        accountService.saveAccount(accountActive);
        return new ResponseEntity<>("Tu cuenta fue eliminada",HttpStatus.ACCEPTED);

    }

}