package com.santander.homebanking.controllers;

import com.santander.homebanking.dtos.ClientDTO;
import com.santander.homebanking.dtos.LoanApplicationDTO;
import com.santander.homebanking.dtos.LoanDTO;
import com.santander.homebanking.models.CardColor;
import com.santander.homebanking.models.CardType;
import com.santander.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class LoanController {
    @Autowired
    LoanService loanService;

    @GetMapping(value="/loans")
    public List<LoanDTO> getLoans(Authentication authentication) {
       return loanService.getLoans(authentication);
    }

    @Transactional
    @PostMapping(value = "/loans")
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){

                ArrayList<Object> response=loanService.createLoan(authentication,loanApplicationDTO);
                return new ResponseEntity<>(response.get(1), HttpStatus.valueOf((Integer)response.get(2)));

    }
}
