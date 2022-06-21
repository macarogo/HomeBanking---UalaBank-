package com.mindhub.homebanking;

import com.mindhub.homebanking.Repository.*;
import com.mindhub.homebanking.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.AccountType.AHORRO;
import static com.mindhub.homebanking.models.AccountType.CORRIENTE;
import static com.mindhub.homebanking.models.CardColor.*;
import static com.mindhub.homebanking.models.Transactiontype.CREDITO;
import static com.mindhub.homebanking.models.Transactiontype.DEBITO;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {

			Client client1 = new Client("Lorenzo", "Malbel","malbel@mindhub.com", passwordEncoder.encode("mainhub1"));
			clientRepository.save(client1);
			Client client2 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("mainhub2"));
			clientRepository.save(client2);
			Client client3 = new Client("admin", "admin", "admin@mindhub.com", passwordEncoder.encode("adminmainhub3"));
			clientRepository.save(client3);


			Account cuenta1 = new Account(client1, "VIN-001", LocalDateTime.now(), 5000.,true,AHORRO);
			Account cuenta2 = new Account(client1,"VIN-002", LocalDateTime.now().plusDays(1), 7500,true, CORRIENTE);
			accountRepository.save(cuenta1);
			accountRepository.save(cuenta2);

			Account cuenta3 = new Account(client2, "vin-003", LocalDateTime.now(), 10000,true, AHORRO);
			Account cuenta4 = new Account(client2, "vin-004", LocalDateTime.now(),15000,true, CORRIENTE);
			accountRepository.save(cuenta3);
			accountRepository.save(cuenta4);

			// save a couple of customers

			Transaction transaction1 = new Transaction(DEBITO, -5000.56, "Pago por Electricidad", LocalDate.now().plusDays(1), true, 5000,cuenta1);
			transactionRepository.save(transaction1);
			Transaction transaction2 = new Transaction(DEBITO, -886.56, "Pago por Agua", LocalDate.now().plusDays(1), true,4113.44,cuenta1);
			transactionRepository.save(transaction2);
			Transaction transaction3 = new Transaction(CREDITO, 10000.56, "Pago por Prestación de Servicios", LocalDate.now(), true,14113.44,cuenta1);
			transactionRepository.save(transaction3);

			Transaction transaction4 = new Transaction(DEBITO, -4052.56, "Pago por Electricidad", LocalDate.now().plusDays(1), true, 7500,cuenta2);
			transactionRepository.save(transaction4);
			Transaction transaction5 = new Transaction(DEBITO, -1086.56, "Pago por Agua", LocalDate.now().plusDays(1), true, cuenta1.getBalance(),cuenta2);
			transactionRepository.save(transaction5);
			Transaction transaction6 = new Transaction(CREDITO, 20000.56, "Pago por Prestación de Servicios", LocalDate.now(), true, cuenta1.getBalance(),cuenta2);
			transactionRepository.save(transaction6);

			Transaction transaction7 = new Transaction(DEBITO, -500.86, "Uber", LocalDate.now(), true, 10000,cuenta3);
			transactionRepository.save(transaction7);
			Transaction transaction8 = new Transaction(CREDITO, 20000.86, "Pago por prestacion de Servicios", LocalDate.now(), true, cuenta1.getBalance(),cuenta3);
			transactionRepository.save(transaction8);

			Transaction transaction9 = new Transaction(DEBITO, -500.86, "Uber", LocalDate.now(), true, 15000,cuenta4);
			transactionRepository.save(transaction9);
			Transaction transaction10 = new Transaction(CREDITO, 20000.86, "Pago por prestacion de Servicios", LocalDate.now(), true, cuenta1.getBalance(),cuenta4);
			transactionRepository.save(transaction10);

			Loan Hipotecario = new Loan ("Hipotecario", 500000, 30,List.of(12, 24, 36, 48, 60));
			Loan Personal = new Loan ("Personal", 100000, 10,List.of(6,12,24));
			Loan Automotriz = new Loan ("Automotriz", 300000, 20,List.of(6,12,24,36));
			loanRepository.save(Hipotecario);
			loanRepository.save(Personal);
			loanRepository.save(Automotriz);


			ClientLoan loan1 = new ClientLoan(400000, 60, client1, Hipotecario);
			clientLoanRepository.save(loan1);
			ClientLoan loan2 = new ClientLoan(500000, 12, client1, Personal);
			clientLoanRepository.save(loan2);
			//ClientLoan loan3 = new ClientLoan(200000, 36, client1, Automotriz);
			//clientLoanRepository.save(loan3);


			Card card1 = new Card(client1.getFirstName()+" "+client1.getLastName(),CardType.DEBITO,GOLD,"1234-2134-1234-2596", 112, LocalDate.now(), LocalDate.now().plusYears(-5),true, false,client1 );
			cardRepository.save(card1);
			Card card2 = new Card(client1.getFirstName()+" "+client1.getLastName(),CardType.CREDITO,TITANIUM,"1174-1834-1245-9687", 115, LocalDate.now(), LocalDate.now().plusYears(5),true,false,client1 );
			cardRepository.save(card2);
			Card card3 = new Card(client2.getFirstName()+" "+client2.getLastName(),CardType.CREDITO,SILVER,"0154-1954-1675-9377", 110, LocalDate.now(), LocalDate.now().plusYears(5),true, false,client2 );
			cardRepository.save(card3);

			//CreditCardLimited limitedGold = new CreditCardLimited(CardColor.GOLD, 100000);

		};
	}

}
