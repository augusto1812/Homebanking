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
    MessageService messageService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;



    public ArrayList<Object> createTransaction(Authentication authentication,
                                                    Double amount,
                                                    String description,
                                                    String accountFrom,
                                                    String accountTo){
        //Verificar que los parámetros no estén vacíos
        if(amount.isNaN() || description.isBlank() || accountFrom.isBlank() || accountTo.isBlank()){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.emptyValues"),403));
        }

        //Verificar que los números de cuenta no sean iguales
        if(accountFrom.equals(accountTo)){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.equalAccounts"),403));
        }

        //Verificar que exista la cuenta de origen
        if (accountRepository.findByNumber(accountFrom).orElse(null) == null){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.missingAccountFrom"),403));
        }
        //Verificar que la cuenta de origen pertenezca al cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName()).orElse(null);
        if(accountRepository.findAccountsByClient(client).size() == 0 ){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.invalidAccountFrom"),403));
        }
        //Verificar que exista la cuenta de destino
        if (accountRepository.findByNumber(accountTo).orElse(null) == null){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.missingAccountTo"),403));
        }

        //Verificar que la cuenta de origen y de destino son del mismo tipo (accountType) (CA y CC)
        if(!(accountRepository.findByNumber(accountFrom).orElse(null).getAccountType().equals(accountRepository.findByNumber(accountTo).orElse(null).getAccountType()))){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.differentAccountType"),403));
        }

        //Verificar que la cuenta de origen y de destino son del mismo tipo de divisa (currencyType)
        if(!(accountRepository.findByNumber(accountFrom).orElse(null).getCurrencyType().equals(accountRepository.findByNumber(accountTo).orElse(null).getCurrencyType()))){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.differentCurrencyType"),403));
        }

        //Verificar que la cuenta de origen tenga el monto disponible.
        Double availableAmountFrom = accountRepository.findByNumber(accountFrom).orElse(null).getBalance();
        if(availableAmountFrom < amount){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.poorMoney"),403));
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
        return new ArrayList<>(Arrays.asList(0,messageService.getMessage("transaction.createTransaction.created"),201));
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
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.emptyValues"),403));
        }

        //Verificar que los números de cuenta no sean iguales
        if(accountFrom.equals(accountTo)){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.equalAccounts"),403));
        }

        //Verificar que exista la cuenta de origen
        Account accFrom = accountRepository.findByNumber(accountFrom).orElse(null);
        if (accFrom == null){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.missingAccountFrom"),403));
        }

        //Verificar que exista la cuenta de destino
        Account accTo = accountRepository.findByNumber(accountTo).orElse(null);
        if (accTo == null){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.missingAccountTo"),403));
        }

        //Verificar que la cuenta de origen y destino pertenezca al cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName()).orElse(null);
        if(accountRepository.findByClientAndNumber(client, accountFrom).orElse(null) ==  null || accountRepository.findByClientAndNumber(client, accountTo).orElse(null) ==  null){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransactionBuySaleCurr.missingAccountFromOrAccountTo"),403));
        }

        //Verificar que la cuenta de origen y de destino son del mismo tipo (accountType) (CA y CC)
        if(!(accFrom.getAccountType().equals(accTo.getAccountType()))){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.differentAccountType"),403));
        }

        //Verificar si la accFrom es del tipo currSale
        if(!(accFrom.getCurrencyType().equals(CurrencyType.valueOf(currSale)))){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransactionBuySaleCurr.accFromTypeWrong"),403));
        }

        //Verificar si la accTo es del tipo currBuy
        if(!(accTo.getCurrencyType().equals(CurrencyType.valueOf(currBuy)))){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransactionBuySaleCurr.accToTypeWrong"),403));
        }

        //Verificar que la cuenta de origen tenga el monto disponible.
        Double availableAmountFrom = accountRepository.findByNumber(accountFrom).orElse(null).getBalance();
        if(availableAmountFrom < amountSale){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.poorMoney"),403));
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
        return new ArrayList<>(Arrays.asList(0,messageService.getMessage("transaction.createTransaction.created"),201));
    }

}
