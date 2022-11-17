package com.santander.homebanking.controllers;


import com.santander.homebanking.models.CardColor;
import com.santander.homebanking.models.CardType;
import com.santander.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/api")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @Transactional
    @PostMapping(value = "/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    @RequestParam Double amount,
                                                    @RequestParam String description,
                                                    @RequestParam String fromAccountNumber,
                                                    @RequestParam String toAccountNumber){
        ArrayList<Object> response = transactionService.createTransaction(authentication,amount,description,fromAccountNumber,toAccountNumber);

        return new ResponseEntity<>((String)response.get(1), HttpStatus.valueOf((Integer)response.get(2)));
    }
}
