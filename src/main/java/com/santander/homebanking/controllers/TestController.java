package com.santander.homebanking.controllers;


import com.santander.homebanking.dtos.CardDTO;
import com.santander.homebanking.dtos.ClientDTO;
import com.santander.homebanking.dtos.TransactionDTO;
import com.santander.homebanking.models.Loan;
import com.santander.homebanking.repositories.AccountRepository;
import com.santander.homebanking.repositories.CardRepository;
import com.santander.homebanking.repositories.LoanRepository;
import com.santander.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private MessageSource messages;

    @GetMapping("/saludar")
    public String mensaje(){
        String mensaje = messages.getMessage("saludo.pais",null, LocaleContextHolder.getLocale());
        return mensaje;
    }

//    @Autowired
//    private CardRepository cardRepository;
//
//    @Autowired
//    private LoanRepository loanRepository;
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @RequestMapping(value = "/all", method = RequestMethod.GET)
//    public String getAllBooks() {
//        return "Lista de libros";
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public String getBook(@PathVariable int id) {
//        return "Id del libro: " + id;
//    }
//
//    @GetMapping(value = "/cards/cvv/{cvv}")
//    public List<CardDTO> getCardsByCardHolder(@PathVariable int cvv){
//        return cardRepository.findByCvvOrderByCvvDesc(cvv).stream().map(CardDTO::new).collect(toList());
//    }
//
//    @GetMapping(value = "/cards/number/{number}")
//    public CardDTO getCardByNumber(@PathVariable String number) {
//        return cardRepository.findByNumber(number).map(CardDTO::new).orElse(null);
//    }
//
//    @GetMapping(value = "/loans/name/{name}")
//    public List<Loan> getLoanByName(@PathVariable String name){
//        return loanRepository.findByNameOrderByNameDesc(name);
//    }
//
//    @GetMapping(value = "/cards/maxamount/{maxamount}")
//    public Loan getLoanByMaxAmount(@PathVariable double maxamount) {
//        return loanRepository.findByMaxAmmount(maxamount).orElse(null);
//    }
//
//    @GetMapping("/transactions/date/{date}")
//    public List<TransactionDTO> getTransactionByDateGreaterThan(@PathVariable String date){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        return transactionRepository.findByDateGreaterThan(LocalDateTime.parse(date,formatter)).stream().map(TransactionDTO::new).collect(toList());
//    }
//


}
