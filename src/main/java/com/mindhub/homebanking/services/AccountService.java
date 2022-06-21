package com.mindhub.homebanking.services;

import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccount();
    AccountDTO getAccounts(Long id);
    void saveAccount(Account account);

    String accountValidate();

    Account getAccountByNumber(String accountNumber);

    Account getAccountById(Long id);

    Account findById (Long id);

}
