package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.DTO.CardApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import utils.CardUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.Transactiontype.DEBITO;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> newCard(@RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication authentication) {
        Client client = clientService.getclientCurrent(authentication);

        Set<Card> card = client.getCards();
        card = card.stream().filter(card1 -> card1.getType().equals(cardType)).collect(Collectors.toSet());

        Set<Card> colorcard ;
        colorcard = card.stream().filter(card1 -> card1.getColor().equals(cardColor)).collect(Collectors.toSet());


        if (card.size() >= 3) {
            return new ResponseEntity<>("Cannot have more than 3 cards of the same type", HttpStatus.FORBIDDEN);
        }

        if (colorcard.size() >= 1){
            return new ResponseEntity<>("No puede tener 2 del mismo tipo de card", HttpStatus.FORBIDDEN);
        }

        Card newCard = new Card(client.getFirstName() + " " + client.getLastName() , cardType , cardColor,
                cardService.cardValidate(), CardUtils.getRandomNumber(100, 999), LocalDate.now(), LocalDate.now().plusYears(5), true, false,client);
        cardService.saveCard(newCard);
        return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);

    }


    @PatchMapping("/clients/current/cards/{id}")
    public ResponseEntity<Object> ocultarCard(@PathVariable Long id){
     Card cardActive = cardService.getCardById(id);
     cardActive.setActive(false);
     cardService.saveCard(cardActive);
     return new ResponseEntity<>("Tu tarjeta fue eliminada",HttpStatus.ACCEPTED);

    }


    @PatchMapping(path = "/cards/expired/{id}")
    public ResponseEntity<Object> expiredCard(@PathVariable long id){
        Card card = cardService.getCardById(id);

        if(card == null){
            return new ResponseEntity<>("No existe ese id", HttpStatus.FORBIDDEN);
        }


        card.setExpired(true);
        cardService.saveCard(card);
        return new ResponseEntity<>("La tarjeta se encuentra vencida",HttpStatus.ACCEPTED);

    }


    @CrossOrigin
    @Transactional
    @PostMapping(path = "/cards/newPago")
    public ResponseEntity<Object> cardPago(@RequestBody CardApplicationDTO cardApplicationDTO){

        if (cardApplicationDTO.getNumber().isEmpty()){
            return new ResponseEntity<>("Esta vacio el campo", HttpStatus.FORBIDDEN);
        }

        if (cardApplicationDTO.getAmount() < 0 ){
            return new ResponseEntity<>("Su monto tiene que ser mayor a cero", HttpStatus.FORBIDDEN);
        }

        if (cardApplicationDTO.getEmail().isEmpty()){
            return new ResponseEntity<>("Su campo de email esta vacio", HttpStatus.FORBIDDEN);
        }

        if (cardApplicationDTO.getDescripcion().isEmpty()){
            return new ResponseEntity<>("Su campo de descripcion esta vacio", HttpStatus.FORBIDDEN);
        }

        if (cardApplicationDTO.getCvv() < 0){
            return new ResponseEntity<>("Su campo de cvv esta vacio", HttpStatus.FORBIDDEN);
        }

        Card card = cardService.getCardByNumber(cardApplicationDTO.getNumber());

        if (card == null){
            return new ResponseEntity<>("La tarjeta no existe", HttpStatus.FORBIDDEN);
        }

        Client client = card.getClient();

        if (!client.getEmail().equals(cardApplicationDTO.getEmail())){
            return new ResponseEntity<>("La tarjeta no te pertenece", HttpStatus.FORBIDDEN);
        }

        Account account = client.getAccount().stream().filter(account1 -> account1.getBalance() >= cardApplicationDTO.getAmount()).findFirst().orElse(null);

        if (account == null){
            return new ResponseEntity<>("No tenes cuenta o tu saldo es insuficiente", HttpStatus.FORBIDDEN);
        }

        LocalDate fechaDeHoy = LocalDate.now();

        if (card.getTruDate().isBefore(fechaDeHoy)){
            return new ResponseEntity<>("La tarjeta esta vencida", HttpStatus.FORBIDDEN);
        }

        if (card.getCvv() != cardApplicationDTO.getCvv()){
            return new ResponseEntity<>("Su CVV es incorrecto", HttpStatus.FORBIDDEN);
        }

        account.setBalance(account.getBalance() - cardApplicationDTO.getAmount());
        accountService.saveAccount(account);

        Transaction transactionCard = new Transaction(DEBITO, cardApplicationDTO.getAmount(), cardApplicationDTO.getDescripcion(), LocalDate.now(), true, account.getBalance(),account);
        transactionService.saveTransaction(transactionCard);

        return new ResponseEntity<>("Tu pago fue exitoso", HttpStatus.CREATED);

    }

}
