package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.Repository.TransactionRepository;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }

}
