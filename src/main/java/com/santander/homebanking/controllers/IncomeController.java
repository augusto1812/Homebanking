package com.santander.homebanking.controllers;



import com.santander.homebanking.dtos.LongTermIncomeDTO;
import com.santander.homebanking.services.IncomeService;
import com.santander.homebanking.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class IncomeController {

    @Autowired
    IncomeService incomeService;
    @Autowired
    private MessageService messageService;

    @PostMapping("/investments")
    public ResponseEntity<Object> addLongTermIncome(@RequestBody LongTermIncomeDTO longTermIncomeDTO)
    {
        String result = incomeService.generatorIncome(longTermIncomeDTO);
        if(result == "accepted"){
         return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(messageService.getMessage(result), HttpStatus.FORBIDDEN);

    }

    @GetMapping("/investments")
    public List<LongTermIncomeDTO> getAllLongIncomes(Authentication authentication){
        return incomeService.getAllLongIncomes(authentication);
    }

}

