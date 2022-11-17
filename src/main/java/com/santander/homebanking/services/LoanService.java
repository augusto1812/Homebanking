package com.santander.homebanking.services;

import com.santander.homebanking.dtos.LoanApplicationDTO;
import com.santander.homebanking.dtos.LoanDTO;
import com.santander.homebanking.models.*;
import com.santander.homebanking.repositories.*;
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
import java.util.List;
import java.util.stream.Collectors;
@Service
public class LoanService {

    @Autowired
    LoanRepository loanRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    MessageService messageService;


    public ArrayList<Object> createLoan(Authentication authentication, LoanApplicationDTO loanApplicationDTO) {
//        Verificar que los datos sean correctos, es decir no estén vacíos, que el monto no sea 0 o que las cuotas no sean 0.
        //id del préstamo, monto, cuotas y número de cuenta de destino
        Long idLoan = loanApplicationDTO.getLoanId();
        Double amount = loanApplicationDTO.getAmount();
        Integer payments = loanApplicationDTO.getPayments();
        String accountToNumber = loanApplicationDTO.getToAccountNumber();
        Account accountTo = accountRepository.findByNumber(accountToNumber).orElse(null);
        if(accountTo.getCurrencyType()!=CurrencyType.ARS){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("loan.createLoan.accountToDontARG"),403));

        }
        if (idLoan.toString().isBlank() || amount.isNaN() || payments.toString().isBlank() || accountToNumber.isBlank()){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("loan.createLoan.emptyValues"),403));

        }
//      Verificar que el préstamo exista
        Loan loan = loanRepository.findById(idLoan).orElse(null);
        if(loan == null){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("loan.createLoan.missingLoan"),403));

        }
//        Verificar que el monto solicitado no exceda el monto máximo del préstamo
        if(amount > loan.getMaxAmount()){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("loan.createLoan.invalidAmount"),403));

        }
//        Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
        if(!loan.getPayments().contains(payments) ){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("loan.createLoan.missingPayments"),403));

        }
//        Verificar que la cuenta de destino exista

        if(accountTo == null){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("loan.createLoan.accountToDoesntExist"),403));

        }
//        Verificar que la cuenta de destino pertenezca al cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName()).orElse(null);
        Client clientAccount = accountTo.getClient();
        if(client != clientAccount){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("loan.createLoan.invalidAccountTo"),403));

        }
//        Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
        Double incrementedAmount = amount * 1.2;
        ClientLoan newClientLoan = new ClientLoan(incrementedAmount,payments,client,loan);
//        Se debe crear una transacción “CREDIT” asociada a la cuenta de destino (el monto debe quedar positivo) con la descripción concatenando el nombre del préstamo y la frase “loan approved”
        String loanDescription = loan.getName() + " loan aproved.";
        Transaction credit = new Transaction(TransactionType.CREDIT,amount,loanDescription,accountTo);
//        Se debe actualizar la cuenta de destino sumando el monto solicitado.
        Double newBalance = accountTo.getBalance() + amount;
        accountTo.setBalance(newBalance);

        clientLoanRepository.save(newClientLoan);
        accountRepository.save(accountTo);
        transactionRepository.save(credit);
        return new ArrayList<>(Arrays.asList(0,messageService.getMessage("loan.createLoan.created"),201));

    }

    public List<LoanDTO> getLoans(Authentication authentication){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }
}
