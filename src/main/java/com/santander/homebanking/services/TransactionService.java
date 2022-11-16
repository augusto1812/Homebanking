package com.santander.homebanking.services;

import com.santander.homebanking.models.*;
import com.santander.homebanking.repositories.AccountRepository;
import com.santander.homebanking.repositories.ClientRepository;
import com.santander.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    private MessageService messageService;



    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    Double amount,
                                                    String description,
                                                    String accountFrom,
                                                    String accountTo){
        //Verificar que los parámetros no estén vacíos
        if(amount.isNaN() || description.isBlank() || accountFrom.isBlank() || accountTo.isBlank()){
            return new ResponseEntity<>(messageService.getMessage("transaction.createTransaction.emptyValues"), HttpStatus.FORBIDDEN);
        }

        //Verificar que los números de cuenta no sean iguales
        if(accountFrom.equals(accountTo)){
            return new ResponseEntity<>(messageService.getMessage("transaction.createTransaction.equalAccounts"), HttpStatus.FORBIDDEN);
        }

        //Verificar que exista la cuenta de origen
        if (accountRepository.findByNumber(accountFrom).orElse(null) == null){
            return new ResponseEntity<>(messageService.getMessage("transaction.createTransaction.missingAccountFrom"), HttpStatus.FORBIDDEN);
        }
        //Verificar que la cuenta de origen pertenezca al cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName()).orElse(null);
        if(accountRepository.findAccountsByClient(client).size() == 0 ){
            return new ResponseEntity<>(messageService.getMessage("transaction.createTransaction.invalidAccountFrom"), HttpStatus.FORBIDDEN);
        }
        //Verificar que exista la cuenta de destino
        if (accountRepository.findByNumber(accountTo).orElse(null) == null){
            return new ResponseEntity<>(messageService.getMessage("transaction.createTransaction.missingAccountTo"), HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de origen tenga el monto disponible.
        Double availableAmountFrom = accountRepository.findByNumber(accountFrom).orElse(null).getBalance();
        if(availableAmountFrom < amount){
            return new ResponseEntity<>(messageService.getMessage("transaction.createTransaction.poorMoney"), HttpStatus.FORBIDDEN);
        }
        //Crear transacción “DEBIT” asociada a la cuenta de origen
        Account accFrom = accountRepository.findByNumber(accountFrom).orElse(null);
        Transaction debit = new Transaction(TransactionType.DEBIT,-amount,description + " transfered money to "+ accountTo, accFrom);

        // Crear transaccion “CREDIT” asociada a la cuenta de destino.
        Account accTo = accountRepository.findByNumber(accountTo).orElse(null);
        Transaction credit = new Transaction(TransactionType.CREDIT,amount,description + " transfered money from "+ accountFrom,accTo);
        //A la cuenta de origen se le restará el monto indicado en la petición
        accFrom.setBalance(availableAmountFrom-amount);
        // cuenta de destino se le sumará el mismo monto.
        Double  availableAmountTo = accountRepository.findByNumber(accountTo).orElse(null).getBalance();
        accTo.setBalance(availableAmountTo + amount);

        accountRepository.save(accFrom);
        accountRepository.save(accTo);
        transactionRepository.save(debit);
        transactionRepository.save(credit);
        return new ResponseEntity<>(messageService.getMessage("transaction.createTransaction.created"),HttpStatus.CREATED);
    }

    public String transactionIncome( TransactionType type, Double amount, String description, Account account){

        if( type== null || amount.isNaN() || description.isBlank() || account == null){
            return "general.missingData";
        }

        if( accountRepository.findById(account.getId()).orElse(null) == null){
            return "transaction.createTransaction.invalidAccountFrom";
        }

        /*  OK  */
        Transaction transaction = new Transaction( type, amount, description, account);
        transactionRepository.save(transaction);
        account.setBalance(account.getBalance() + amount);
        account.addTransaction(transaction);
        accountRepository.save(account);
        return "accepted";
    }
}
