package com.santander.homebanking.controllers;


import com.santander.homebanking.dtos.CashbackDTO;
import com.santander.homebanking.services.CashbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class CashbackController {

    @Autowired
    CashbackService cashbackService;

    @GetMapping(value="/cashbacks")
    public List<CashbackDTO> getCashbacks() {
        return cashbackService.getCashbacks();
    }

    @GetMapping(value="/cashbacks/{id}")
    public CashbackDTO getCashback(@PathVariable(name="id") Long id) {
        return cashbackService.getCashback(id);
    }
}
