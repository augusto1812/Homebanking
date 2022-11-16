package com.santander.homebanking.controllers;

import com.santander.homebanking.dtos.ShopDTO;
import com.santander.homebanking.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class ShopController {

    @Autowired
    ShopService shopService;

    @Transactional
    @PostMapping(path = "/shop")
    public ResponseEntity<Object> shop(@RequestBody ShopDTO shopDTO){
        return shopService.shop(shopDTO);
    }

}
