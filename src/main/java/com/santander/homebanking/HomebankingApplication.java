package com.santander.homebanking;

import com.santander.homebanking.models.*;
import com.santander.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableJpaAuditing
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository repository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, DiscountRepository discountRepository, CashbackRepository cashbackRepository) {
		return (args) -> {

			// save a couple of customers
			LocalDateTime fechaHoy = LocalDateTime.now();
			LocalDateTime fechaManiana = LocalDateTime.now().plusDays(1);
			Client cliente1 = new Client("Jack", "Bauer", "JackBauer@hotmail.com", passwordEncoder.encode("123"));
			repository.save(cliente1);

			Account cuenta1 = new Account("VIN007",5000,cliente1);
			Account cuenta2 = new Account("VIN008",7500,cliente1);
			accountRepository.save(cuenta1);
			accountRepository.save(cuenta2);
			cliente1.addAccount(cuenta1);

			transactionRepository.save(new Transaction(TransactionType.CREDIT, 200.00, "prueba01", cuenta1));
			transactionRepository.save(new Transaction(TransactionType.DEBIT, 2000.00, "prueba002",cuenta1));
			transactionRepository.save(new Transaction(TransactionType.DEBIT, 300.00, "prueba0003", cuenta2));

			Client cliente2 = new Client("Juan", "Gonzalez", "JuanGonzalez@hotmail.com", passwordEncoder.encode("123"));
			repository.save(cliente2);

			Account cuenta3 = new Account("VIN009",9000,cliente2);
			Account cuenta4 = new Account("VIN008",70,cliente2);
			accountRepository.save(cuenta3);
			accountRepository.save(cuenta4);

			transactionRepository.save(new Transaction(TransactionType.CREDIT, 1000200.00, "prueba04", cuenta3));
			transactionRepository.save(new Transaction(TransactionType.DEBIT, 2000.00, "prueba0025",cuenta4));
			transactionRepository.save(new Transaction(TransactionType.DEBIT, 300.00, "prueba0006",cuenta4));

			Client cli_aux = repository.findByEmail("melba@mindhub.com").orElse(null);

			cli_aux.setPassword(passwordEncoder.encode("123"));

			repository.save(cli_aux);

			Client cli_aux2 = repository.findByEmail("irene@mindhub.com").orElse(null);

			cli_aux2.setPassword(passwordEncoder.encode("123"));

			repository.save(cli_aux2);

			//Loan loan1 = new Loan("Hipotecario",500000,new ArrayList<>(Arrays.asList(6,12,24,48)));

			//ClientLoan clientLoan1 = new ClientLoan(500000,new ArrayList<>(Arrays.asList(6,12,24,48)),cliente1,loan1);
			// commit
			// commit


			Map<CardColor,Double> mapG = new HashMap<>();
			mapG.put(CardColor.GOLD,50.0);
			Map<CardColor,Double> mapS = new HashMap<>();
			mapS.put(CardColor.SILVER,100.0);
			Map<CardColor,Double> mapT = new HashMap<>();
			mapT.put(CardColor.TITANIUM,200.0);
			Discount discount1 = new Discount(SectorType.CLOTHING,fechaHoy,fechaManiana,mapG);
			Discount discount2 = new Discount(SectorType.CLOTHING,fechaHoy,fechaManiana,mapS);
			Discount discount3 = new Discount(SectorType.CLOTHING,fechaHoy,fechaManiana,mapT);
			Discount discount4 = new Discount(SectorType.PHARMACY,fechaHoy,fechaManiana,mapG);
			Discount discount5 = new Discount(SectorType.PHARMACY,fechaHoy,fechaManiana,mapS);
			Discount discount6 = new Discount(SectorType.PHARMACY,fechaHoy,fechaManiana,mapT);

			discountRepository.save(discount1);
			discountRepository.save(discount2);
			discountRepository.save(discount3);
			discountRepository.save(discount4);
			discountRepository.save(discount5);
			discountRepository.save(discount6);


			Cashback cashback1 = new Cashback(CardColor.TITANIUM,CardType.DEBIT,0.01);
			Cashback cashback2 = new Cashback(CardColor.SILVER,CardType.DEBIT,0.005);
			Cashback cashback3 = new Cashback(CardColor.GOLD,CardType.DEBIT,0.002);
			Cashback cashback4 = new Cashback(CardColor.TITANIUM,CardType.CREDIT,0.03);
			Cashback cashback5 = new Cashback(CardColor.SILVER,CardType.CREDIT,0.015);
			Cashback cashback6 = new Cashback(CardColor.GOLD,CardType.CREDIT,0.007);

			cashbackRepository.save(cashback1);
			cashbackRepository.save(cashback2);
			cashbackRepository.save(cashback3);
			cashbackRepository.save(cashback4);
			cashbackRepository.save(cashback5);
			cashbackRepository.save(cashback6);
		};
	}
}
