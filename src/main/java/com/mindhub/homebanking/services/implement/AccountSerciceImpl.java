package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.Repository.AccountRepository;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static utils.AccountUtils.getRandomNumber;

@Service
public class AccountSerciceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<AccountDTO> getAccount() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccounts(Long id) {
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public String accountValidate(){
        String account = "VIN-" + getRandomNumber(1,10000000);
        System.out.println(account);
        while (accountRepository.findByNumber(account) !=null){
            account = "VIN-" + getRandomNumber(1,10000000);
        }
        return account;

    }

    @Override
    public Account getAccountByNumber(String accountNumber){
        return accountRepository.findByNumber(accountNumber);
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }
}