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
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class TransactionService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    private MessageSource message;

    public String  getMensaje (String mensaje)
    {
        return   message.getMessage(mensaje,null, LocaleContextHolder.getLocale());

    }

    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    Double amount,
                                                    String description,
                                                    String accountFrom,
                                                    String accountTo){
        //Verificar que los parámetros no estén vacíos
        if(amount.isNaN() || description.isBlank() || accountFrom.isBlank() || accountTo.isBlank()){
            return new ResponseEntity<>(getMensaje("transaction.createTransaction.emptyValues"), HttpStatus.FORBIDDEN);
        }

        //Verificar que los números de cuenta no sean iguales
        if(accountFrom.equals(accountTo)){
            return new ResponseEntity<>(getMensaje("transaction.createTransaction.equalAccounts"), HttpStatus.FORBIDDEN);
        }

        //Verificar que exista la cuenta de origen
        if (accountRepository.findByNumber(accountFrom).orElse(null) == null){
            return new ResponseEntity<>(getMensaje("transaction.createTransaction.missingAccountFrom"), HttpStatus.FORBIDDEN);
        }
        //Verificar que la cuenta de origen pertenezca al cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName()).orElse(null);
        if(accountRepository.findAccountsByClient(client).size() == 0 ){
            return new ResponseEntity<>(getMensaje("transaction.createTransaction.invalidAccountFrom"), HttpStatus.FORBIDDEN);
        }
        //Verificar que exista la cuenta de destino
        if (accountRepository.findByNumber(accountTo).orElse(null) == null){
            return new ResponseEntity<>(getMensaje("transaction.createTransaction.missingAccountTo"), HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de origen tenga el monto disponible.
        Double availableAmountFrom = accountRepository.findByNumber(accountFrom).orElse(null).getBalance();
        if(availableAmountFrom < amount){
            return new ResponseEntity<>(getMensaje("transaction.createTransaction.poorMoney"), HttpStatus.FORBIDDEN);
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
        return new ResponseEntity<>(getMensaje("transaction.createTransaction.created"),HttpStatus.CREATED);
    }


    public ArrayList<Object> createTransactionBuySaleCurr(
            Authentication authentication,
            Double amountSale,
            Double amountBuy,
            String currBuy,
            String currSale,
            String accountFrom,
            String accountTo){


        //Verificar que los parámetros no estén vacíos
        if(amountSale.isNaN() || amountBuy.isNaN() || accountFrom.isBlank() || accountTo.isBlank()){
            return new ArrayList<>(Arrays.asList(1,getMensaje("transaction.createTransaction.emptyValues"),403));
        }

        //Verificar que los números de cuenta no sean iguales
        if(accountFrom.equals(accountTo)){
            return new ArrayList<>(Arrays.asList(1,getMensaje("transaction.createTransaction.equalAccounts"),403));
        }

        //Verificar que exista la cuenta de origen
        Account accFrom = accountRepository.findByNumber(accountFrom).orElse(null);
        if (accFrom == null){
            return new ArrayList<>(Arrays.asList(1,getMensaje("transaction.createTransaction.missingAccountFrom"),403));
        }

        //Verificar que exista la cuenta de destino
        Account accTo = accountRepository.findByNumber(accountTo).orElse(null);
        if (accTo == null){
            return new ArrayList<>(Arrays.asList(1,getMensaje("transaction.createTransaction.missingAccountTo"),403));
        }

        //Verificar que la cuenta de origen y destino pertenezca al cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName()).orElse(null);
        if(accountRepository.findByClientAndNumber(client, accountFrom).orElse(null) ==  null || accountRepository.findByClientAndNumber(client, accountTo).orElse(null) ==  null){
            return new ArrayList<>(Arrays.asList(1,getMensaje("transaction.createTransactionBuySaleCurr.missingAccountFromOrAccountTo"),403));
        }

        //COMPLETAR
        //Verificar si los tipos de cuenta son distintos. CA y CC

        //Verificar si la accFrom es del tipo currSale
        if(!(accFrom.getCurrencyType().equals(CurrencyType.valueOf(currSale)))){
            return new ArrayList<>(Arrays.asList(1,getMensaje("transaction.createTransactionBuySaleCurr.accFromTypeWrong"),403));
        }

        //Verificar si la accTo es del tipo currBuy
        if(!(accTo.getCurrencyType().equals(CurrencyType.valueOf(currBuy)))){
            return new ArrayList<>(Arrays.asList(1,getMensaje("transaction.createTransactionBuySaleCurr.accToTypeWrong"),403));
        }

        //Verificar que la cuenta de origen tenga el monto disponible.
        Double availableAmountFrom = accountRepository.findByNumber(accountFrom).orElse(null).getBalance();
        if(availableAmountFrom < amountSale){
            return new ArrayList<>(Arrays.asList(1,getMensaje("transaction.createTransaction.poorMoney"),403));
        }

        //Crear transacción “DEBIT” asociada a la cuenta de origen
//        Account accFrom = accountRepository.findByNumber(accountFrom).orElse(null);
        Transaction debit = new Transaction(TransactionType.DEBIT,-amountSale,  "Compra de "+ currBuy + ". Depositado en la cuenta " + accountTo, accFrom);

        // Crear transaccion “CREDIT” asociada a la cuenta de destino.
//        Account accTo = accountRepository.findByNumber(accountTo).orElse(null);
        Transaction credit = new Transaction(TransactionType.CREDIT,amountBuy,"Venta de " + currSale + ". Origen de los fondos " + accountFrom,accTo);

        //A la cuenta de origen se le restará el monto indicado en la petición
        accFrom.setBalance(availableAmountFrom-amountSale);

        // cuenta de destino se le sumará el mismo monto.
        Double  availableAmountTo = accountRepository.findByNumber(accountTo).orElse(null).getBalance();
        accTo.setBalance(availableAmountTo + amountBuy);

        accountRepository.save(accFrom);
        accountRepository.save(accTo);
        transactionRepository.save(debit);
        transactionRepository.save(credit);
        return new ArrayList<>(Arrays.asList(0,getMensaje("transaction.createTransaction.created"),201));
    }

}
