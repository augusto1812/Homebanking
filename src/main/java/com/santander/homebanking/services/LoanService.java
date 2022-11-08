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
    MessageSource message;

    public String  getMensaje (String mensaje)
    {
        return   message.getMessage(mensaje,null, LocaleContextHolder.getLocale());

    }
    public ResponseEntity<Object> createLoan(Authentication authentication, LoanApplicationDTO loanApplicationDTO) {
//        Verificar que los datos sean correctos, es decir no estén vacíos, que el monto no sea 0 o que las cuotas no sean 0.
        //id del préstamo, monto, cuotas y número de cuenta de destino
        Long idLoan = loanApplicationDTO.getLoanId();
        Double amount = loanApplicationDTO.getAmount();
        Integer payments = loanApplicationDTO.getPayments();
        String accountToNumber = loanApplicationDTO.getToAccountNumber();
        if (idLoan.toString().isBlank() || amount.isNaN() || payments.toString().isBlank() || accountToNumber.isBlank()){
            return new ResponseEntity<>(getMensaje("loan.createLoan.emptyValues"), HttpStatus.FORBIDDEN);
        }
//      Verificar que el préstamo exista
        Loan loan = loanRepository.findById(idLoan).orElse(null);
        if(loan == null){
            return new ResponseEntity<>(getMensaje("loan.createLoan.missingLoan"), HttpStatus.FORBIDDEN);
        }
//        Verificar que el monto solicitado no exceda el monto máximo del préstamo
        if(amount > loan.getMaxAmmount()){
            return new ResponseEntity<>(getMensaje("loan.createLoan.invalidAmount"), HttpStatus.FORBIDDEN);
        }
//        Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
        if(!loan.getPayments().contains(payments) ){
            return new ResponseEntity<>(getMensaje("loan.createLoan.missingPayments"), HttpStatus.FORBIDDEN);
        }
//        Verificar que la cuenta de destino exista
        Account accountTo = accountRepository.findByNumber(accountToNumber).orElse(null);
        if(accountTo == null){
            return new ResponseEntity<>(getMensaje("loan.createLoan.accountToDoesntExist"), HttpStatus.FORBIDDEN);
        }
//        Verificar que la cuenta de destino pertenezca al cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName()).orElse(null);
        Client clientAccount = accountTo.getClient();
        if(client != clientAccount){
            return new ResponseEntity<>(getMensaje("loan.createLoan.invalidAccountTo"), HttpStatus.FORBIDDEN);
        }
//        Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
        Double incrementedAmount = amount * 1.2;
        ClientLoan newClientLoan = new ClientLoan(incrementedAmount,payments,client,loan);
//        Se debe crear una transacción “CREDIT” asociada a la cuenta de destino (el monto debe quedar positivo) con la descripción concatenando el nombre del préstamo y la frase “loan approved”
        String loanDescription = loan.getName() + " loan aproved.";
        Transaction credit = new Transaction(TransactionType.CREDIT,amount,loanDescription, LocalDateTime.now(),accountTo);
//        Se debe actualizar la cuenta de destino sumando el monto solicitado.
        Double newBalance = accountTo.getBalance() + amount;
        accountTo.setBalance(newBalance);

        clientLoanRepository.save(newClientLoan);
        accountRepository.save(accountTo);
        transactionRepository.save(credit);

        return new ResponseEntity<>(getMensaje("loan.createLoan.created"), HttpStatus.CREATED);
    }

    public List<LoanDTO> getLoans(Authentication authentication){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }
}
