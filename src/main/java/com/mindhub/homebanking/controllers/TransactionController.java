package com.mindhub.homebanking.controllers;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.Transactiontype.CREDITO;
import static com.mindhub.homebanking.models.Transactiontype.DEBITO;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping(path = "/transactions")
    public ResponseEntity<Object>newtransaction(Authentication authentication, @RequestParam Double amount, @RequestParam String description, @RequestParam String accountOrigen, @RequestParam String accountDestination) {
        Client client = clientService.getClientByEmail(authentication.getName());
        Account origenAccount = accountService.getAccountByNumber(accountOrigen);
        Account origenDestination = accountService.getAccountByNumber(accountDestination);
        if (description.isEmpty()){
            return new ResponseEntity<>("No contiene descripcion", HttpStatus.FORBIDDEN);
        }
        if (accountOrigen.isEmpty()){
            return new ResponseEntity<>("No posee esta cuenta", HttpStatus.FORBIDDEN);
        }
        if (accountDestination.isEmpty()){
            return new ResponseEntity<>("No existe", HttpStatus.FORBIDDEN);
        }
        if (amount <= 0 || amount.isNaN() || amount.isInfinite()) {
            return new ResponseEntity<>("Monto menor o igual a 0", HttpStatus.FORBIDDEN);
        }
        if (origenAccount.getBalance() < amount){
            return new ResponseEntity<>("No tiene monto suficiente", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccount().contains(origenAccount)){
            return new ResponseEntity<>("Esta no es tu cuenta", HttpStatus.FORBIDDEN);
        }
        if(accountService.getAccountByNumber(accountOrigen) == null){
            return new ResponseEntity<>("La cuenta que esta queriendo transferiendo no existe", HttpStatus.FORBIDDEN);
        }
        if(origenDestination.equals(origenAccount) ){
            return new ResponseEntity<>("No podes transferir a tu misma cuenta", HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebit = new Transaction(DEBITO, amount * -1, description + " for " + accountDestination, LocalDate.now(), true, origenAccount.getBalance()-amount,origenAccount);
        transactionService.saveTransaction(transactionDebit);
        Transaction transactionCredit = new Transaction(CREDITO, amount , description + " of " +  accountOrigen, LocalDate.now(), true, origenAccount.getBalance()+amount,origenDestination);
        transactionService.saveTransaction(transactionCredit);
        origenAccount.setBalance(origenAccount.getBalance() - amount);
        origenDestination.setBalance(origenDestination.getBalance() + amount);
        accountService.saveAccount(origenAccount);
        accountService.saveAccount(origenDestination);
        return new ResponseEntity<>("transaction created successfully" ,HttpStatus.CREATED);
    }

    @PostMapping(path = "/pdf/{id}")
    public ResponseEntity<Object> createPdf(@PathVariable long id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta) throws FileNotFoundException, DocumentException {
        Account account = accountService.findById(id);
        if(account == null){
            return new ResponseEntity<>("La cuenta no existe", HttpStatus.FORBIDDEN);
        }
        Client client1 = account.getClient();
        Set<Transaction> transaction = account.getTransactions();
        Set<Transaction> transactionSet = transaction.stream().filter(transaction1 -> transaction1.getDate().isBefore(ChronoLocalDate.from(hasta.plusDays(1)))).collect(Collectors.toSet());
        transactionSet.stream().filter(transaction1 -> transaction1.getDate().isAfter(ChronoLocalDate.from(desde)));

        Document document = new Document();
        String ruta = System.getProperty("user.home");
        PdfWriter.getInstance(document, new FileOutputStream(ruta + "/Desktop/Transacciones.pdf"));
        document.open();
        Phrase p = new Phrase("Cuenta:" +account.getNumber());
        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage (100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("Type");
        table.addCell("Monto");
        table.addCell("Fecha");
        table.addCell("Description");
        table.addCell("Saldo Actual");

        transactionSet.stream().forEach(transaction1 -> {
            PdfPCell c1 = new PdfPCell(new Phrase(transaction1.getType() + ""));
            PdfPCell c2 = new PdfPCell(new Phrase(transaction1.getAmount() + ""));
            PdfPCell c3 = new PdfPCell(new Phrase(transaction1.getDate() + ""));
            PdfPCell c4 = new PdfPCell(new Phrase(transaction1.getDescription()));
            PdfPCell c5 = new PdfPCell(new Phrase(transaction1.getBalance() + ""));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            table.addCell(c2);
            table.addCell(c3);
            table.addCell(c4);
            table.addCell(c5);
        });
        document.add(table);
        document.close();
        return new ResponseEntity<>("Creado", HttpStatus.ACCEPTED);
    }
}
