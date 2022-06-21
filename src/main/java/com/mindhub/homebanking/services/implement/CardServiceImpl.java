package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.Repository.CardRepository;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static utils.CardUtils.getRandomNumber;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    CardRepository cardRepository;


    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public String cardValidate() {
        String card = 4309 + "-" + getRandomNumber(1, 10000)+ "-" + getRandomNumber(1, 10000)+ "-" + getRandomNumber(1, 10000);
        System.out.println(card);
        while (cardRepository.findByNumber(card) != null) {
            card = 4309 + "-" + getRandomNumber(1, 10000)+ "-" + getRandomNumber(1, 10000)+ "-" + getRandomNumber(1, 10000);
        }
        return card;

    }

    @Override
    public Card getCardById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card getCardByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

}
