package com.santander.homebanking;

import com.santander.homebanking.models.*;
import com.santander.homebanking.repositories.*;
import com.santander.homebanking.services.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
public class IntegracionJpaTest {

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;


    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Autowired
    CardRepository cardRepository;

    @Test
    void testFindAllClient() {
        List<Client> clients = clientRepository.findAll();
        assertFalse(clients.isEmpty());
    }

    @Test
    void testFindClientById() {
        Optional<Client> client = clientRepository.findById(1L);
        assertTrue(client.isPresent());
        assertEquals("Melba",client.orElseThrow().getFirstName());
    }

    @Test
    void testFindAccountAll() {
        List<Account> accounts = accountRepository.findAll();
        assertFalse(accounts.isEmpty());
    }

    @Test
    void testFindAccountById() {
        Optional<Account> account = accountRepository.findById(1L);
        assertTrue(account.isPresent());
        assertEquals("VIN001",account.orElseThrow().getNumber());
    }

    @Test
    void testFindaAllTransaction() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertFalse(transactions.isEmpty());
    }

    @Test
    void testFindTransactionById(){
        Optional<Transaction> transaction = transactionRepository.findById(1L);
        assertTrue(transaction.isPresent());
        assertEquals(TransactionType.CREDIT, transaction.orElseThrow().getType());
    }

    @Test
    void testFindaAllLoan() {
        List<Loan> loans = loanRepository.findAll();
        assertFalse(loans.isEmpty());
    }

    @Test
    void testFindLoanById(){
        Optional<Loan> loan = loanRepository.findById(1L);
        assertTrue(loan.isPresent());
        assertEquals("Hipotecario", loan.orElseThrow().getName());
    }

    @Test
    void testFindaAllClientLoan() {
        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
        assertFalse(clientLoans.isEmpty());
    }

    @Test
    void testFindClientLoanById(){
        Optional<ClientLoan> clientLoan = clientLoanRepository.findById(1L);
        assertTrue(clientLoan.isPresent());
        assertEquals(400000, clientLoan.orElseThrow().getAmount());
    }

    @Test
    void testFindaAllCard() {
        List<Card> cards = cardRepository.findAll();
        assertFalse(cards.isEmpty());
    }

    @Test
    void testFindCardById(){
        Optional<Card> card = cardRepository.findById(1L);
        assertTrue(card.isPresent());
        assertEquals(990, card.orElseThrow().getCvv());
    }

    @Test
    void testClientSave(){
        Client data = new Client("Pepe","Grillo","pepe@gmail.com","123");
        Client client = clientRepository.save(data);
        assertEquals("Pepe",client.getFirstName());
        assertEquals("Grillo",client.getLastName());
        assertEquals("pepe@gmail.com", client.getEmail());
        assertEquals("123", client.getPassword());

    }

    @Test
    void testAccountSave(){
        Client client = clientRepository.findById(1L).get();
        Account data = new Account("VIN005",5000,client,AccountType.CA,CurrencyType.ARS);
        Account account = accountRepository.save(data);
        assertEquals("VIN005", account.getNumber());
        assertEquals(5000, account.getBalance());
        assertEquals("Melba", account.getClient().getFirstName());
        assertEquals(AccountType.CA, account.getAccountType());
        assertEquals(CurrencyType.ARS, account.getCurrencyType());
    }


}
