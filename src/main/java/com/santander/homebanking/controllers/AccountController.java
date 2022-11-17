package com.santander.homebanking.controllers;

import com.santander.homebanking.dtos.AccountDTO;
import com.santander.homebanking.dtos.ClientDTO;
import com.santander.homebanking.models.Account;
import com.santander.homebanking.models.AccountType;
import com.santander.homebanking.models.Client;
import com.santander.homebanking.models.CurrencyType;
import com.santander.homebanking.repositories.AccountRepository;
import com.santander.homebanking.repositories.ClientRepository;
import com.santander.homebanking.services.AccountService;
import com.santander.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping(value = "/accounts/{id}")
    public AccountDTO getAccount(@PathVariable(value="id") Long id) {
        return accountService.getAccount(id);
    }

    @GetMapping(value = "clients/current/accounts")
    public List<AccountDTO> getCurrentClientAccounts(Authentication authentication){
        return accountService.getCurrentClientAccounts(authentication);
    }

    @PostMapping(value = "clients/current/accounts")
    public ResponseEntity<Object> newAccount(Authentication authentication, @RequestParam AccountType accountType, @RequestParam CurrencyType currencyType){
        ArrayList<Object> response =  clientService.newAccountToClient(authentication, accountType, currencyType);
        return new ResponseEntity<>(response.get(1),HttpStatus.valueOf((Integer)response.get(2)));
    }

//    @GetMapping(value = "/{id}/accounts")
//    public List<AccountDTO> getAllAccountsByClientId(@PathVariable Long id){
//        return accountService.getAllAccountsByClientId(id);
//    }

    @GetMapping(value="/account/getTypesAccount")
    public ResponseEntity<Object> getTypesAccount(){
        ArrayList<Object> response = accountService.getTypesAccount();
        return new ResponseEntity<>(response.get(1),HttpStatus.valueOf((Integer)response.get(2)));
    }




}
