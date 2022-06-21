package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.ClientLoanDTO;
import com.mindhub.homebanking.DTO.LoanApplicationDto;
import com.mindhub.homebanking.DTO.LoanDto;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;

import java.util.List;



import static com.mindhub.homebanking.models.Transactiontype.CREDITO;



@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private LoanService loanService;



    @GetMapping("/clientsloans")
    public List<ClientLoanDTO>getClientsLoans(){
    return loanService.getAllClientLoans();
    }


    @GetMapping("/loans")
    public  List<LoanDto>getLoans(){
        return  loanService.getAllLoans();
    }


    @PostMapping("/newLoan")
    public ResponseEntity<Object> createLoan (@RequestBody Loan loan){
        Loan loan1 = new Loan(loan.getName(), loan.getMaxAmount(),loan.getInterest(),loan.getPayments());
        loanService.saveLoan(loan1);
        return new ResponseEntity<>("Loan Create successfully",HttpStatus.ACCEPTED);
    }

    @Transactional
    @PostMapping(path = "/loans")
    public ResponseEntity<Object> newLoan(@RequestBody LoanApplicationDto loanApplicationDto, Authentication authentication) {
        Client client = clientService.getClientByEmail(authentication.getName());
        Account account = accountService.getAccountByNumber(loanApplicationDto.getAccountDestination());
        Loan loan = loanService.getLoan(loanApplicationDto.getId());

        if (client.getLoans().contains(loan)){
            return new ResponseEntity<>("No puede tener mas de una ves el mismo prestamo", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDto.getAmount() <= 0 || loanApplicationDto.getAmount().isNaN() || loanApplicationDto.getAmount().isInfinite() ){
            return new ResponseEntity<>("El importe debe ser mayor a cero", HttpStatus.FORBIDDEN);
        }

        //if (loanApplicationDto.getLongId() == 0 ){
            //return new ResponseEntity<>("El campo esta vacio", HttpStatus.FORBIDDEN);
        //}

        if (loanApplicationDto.getPayments() <= 0 ){
            return new ResponseEntity<>("La cuotas deben ser mayor a cero", HttpStatus.FORBIDDEN);
        }

        if (loan == null ){
            return new ResponseEntity<>("El prestamo solicitado no se encuetra habilitado", HttpStatus.FORBIDDEN);
        }

        if (loan.getMaxAmount() < loanApplicationDto.getAmount()){
            return new ResponseEntity<>("El monto exede el maximo permitido para ese tipo de prestamo", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplicationDto.getPayments())){
            return new ResponseEntity<>("Cuotas no disponibles para ese tipo de Prestamo", HttpStatus.FORBIDDEN);
        }

        if (account == null ){
            return new ResponseEntity<>("La cuenta desde la que estas queriendo transferir no existe", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccount().contains(account)){
            return new ResponseEntity<>("La cuenta no esta asocidada al cliente", HttpStatus.FORBIDDEN);
        }

        ClientLoan newClientLoan = new ClientLoan(loanApplicationDto.getAmount()+(loanApplicationDto.getAmount() * loan.getInterest()/100), loanApplicationDto.getPayments(), client, loan);
        loanService.saveClientLoan(newClientLoan);

        transactionService.saveTransaction(new Transaction(CREDITO, loanApplicationDto.getAmount() , "Credito aprobado :" + loan.getName(), LocalDate.now(), true, account.getBalance(),account));
        account.setBalance(account.getBalance() + loanApplicationDto.getAmount());

        accountService.saveAccount(account);
        return new ResponseEntity<>("Credito solicitado correctamente" ,HttpStatus.CREATED);

    }


}
