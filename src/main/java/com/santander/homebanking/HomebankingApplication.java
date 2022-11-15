package com.santander.homebanking;

import com.santander.homebanking.models.*;
import com.santander.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository repository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, LongTermIncomeRepository longTermIncomeRepository) {
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

			LongTermIncome longTermIncome = new LongTermIncome(cuenta1,3000.00,PeriodType.PERIOD_30);
			longTermIncomeRepository.save(longTermIncome);
			accountRepository.save(cuenta1);

			//Loan loan1 = new Loan("Hipotecario",500000,new ArrayList<>(Arrays.asList(6,12,24,48)));

			//ClientLoan clientLoan1 = new ClientLoan(500000,new ArrayList<>(Arrays.asList(6,12,24,48)),cliente1,loan1);
			// commit
			// commit

		};
	}
}
