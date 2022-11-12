package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.ClienDTO;
import com.mindhub.homebanking.Repository.*;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
   @Autowired
   ClientRepository clientRepository;
   @Autowired
    CardRepository cardRepository;
   @Autowired
    AccountRepository accountRepository;
   @Autowired
    ClientLoanRepository clientLoanRepository;
   @Autowired
    TransactionRepository transactionRepository;

   @Override
    public List<ClienDTO> getClientsDto(){
        return clientRepository.findAll().stream().map(client -> new ClienDTO(client)).collect(Collectors.toList());
    };

    @Override
    public Client getclientCurrent(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName());
    }

    @Override
    public ClienDTO getclientDto(long id) {
        return clientRepository.findById(id).map(client -> new ClienDTO(client)).orElse(null);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    };

    @Override
    public Client getClientById(long id){
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteClient(long id) {
        Client client = clientRepository.findById(id).orElse(null);
        Set<Card> cards = client.getCards();
        Set<ClientLoan> clientLoans = client.getClientLoans();
        Set<Account> accounts = client.getAccount();
        Set<Transaction> transactions = new HashSet<>();
        accounts.stream().forEach(account -> {
            transactions.addAll(account.getTransactions());
        });

        cards.forEach(card -> cardRepository.delete(card));
        clientLoans.forEach(clientLoan -> clientLoanRepository.delete(clientLoan));
        transactions.forEach(transaction -> transactionRepository.delete(transaction));
        accounts.forEach(account -> accountRepository.delete(account));
        clientRepository.delete(client);
    }
}