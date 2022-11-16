package com.santander.homebanking.controllers;

import com.santander.homebanking.dtos.CurrencyDTO;
import com.santander.homebanking.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@RestController
@RequestMapping(value = "/api")
public class CurrencyController {

    @Autowired
    CurrencyService currencyService;

    //BORRAR
    @GetMapping(value="/currency/dolar")
    public Object dolar(){
        String urlAPI = "https://mercados.ambito.com/dolar/oficial/variacion";
        RestTemplate restTemplate = new RestTemplate();
        Object returnUSD = restTemplate.getForObject(urlAPI, Object.class);

        LinkedHashMap<String, String> returnUSD2 = restTemplate.getForObject(urlAPI, LinkedHashMap.class);

        String compraVal = returnUSD2.get("compra");


        return returnUSD;
    }

    @GetMapping(value="/currency/getPrice/{currBuy}/{currSale}")
    public ResponseEntity<Object> getPrice(
            @PathVariable(name = "currBuy") String currBuy,
            @PathVariable(name = "currSale") String currSale) {
        ArrayList<Object> response = currencyService.getPrice(currBuy.toLowerCase(),currSale.toLowerCase());

        if((Integer)response.get(0) == 0){
            //CurrencyDTO currencyDTO = (CurrencyDTO)response.get(1);
            return new ResponseEntity<>((CurrencyDTO)response.get(1),HttpStatus.valueOf((Integer)response.get(2)));
        }else{
            return new ResponseEntity<>((String)response.get(1),HttpStatus.valueOf((Integer)response.get(2)));
        }

    }

    @PostMapping(value = "/currency/buySaleCurr")
    public ResponseEntity<Object> buySaleCurr(
            Authentication authentication,
            @RequestParam Double amount,
            @RequestParam String currBuy,
            @RequestParam String currSale,
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber){

        ArrayList<Object> response = currencyService.buySaleCurr(
                authentication,
                amount,
                currBuy,
                currSale,
                fromAccountNumber,
                toAccountNumber);

        return new ResponseEntity<>((String)response.get(1),HttpStatus.valueOf((Integer)response.get(2)));

    }


    @GetMapping(value="/currency/getTypesCurrencies")
    public ResponseEntity<Object> getTypesCurrencies(){
        ArrayList<Object> response = currencyService.getTypesCurrencies();
        return new ResponseEntity<>(response.get(1),HttpStatus.valueOf((Integer)response.get(2)));
    }


    @GetMapping(value="/currency/getBuyAmount")
    public ResponseEntity<Object> getBuyAmount(
            @RequestParam Double amount,
            @RequestParam String currBuy,
            @RequestParam String currSale) {
        ArrayList<Object> response = currencyService.getBuyAmount(amount,currBuy.toLowerCase(),currSale.toLowerCase());
        if((Integer)response.get(0) == 0){
            return new ResponseEntity<>((Double)response.get(1),HttpStatus.valueOf((Integer)response.get(2)));
        }else{
            return new ResponseEntity<>((String)response.get(1),HttpStatus.valueOf((Integer)response.get(2)));
        }
    }

    @GetMapping(value="/currency/getSaleAmount")
    public ResponseEntity<Object> getSaleAmount(
            @RequestParam Double amount,
            @RequestParam String currBuy,
            @RequestParam String currSale) {
        ArrayList<Object> response = currencyService.getSaleAmount(amount,currBuy.toLowerCase(),currSale.toLowerCase());
        if((Integer)response.get(0) == 0){
            return new ResponseEntity<>((Double)response.get(1),HttpStatus.valueOf((Integer)response.get(2)));
        }else{
            return new ResponseEntity<>((String)response.get(1),HttpStatus.valueOf((Integer)response.get(2)));
        }
    }

}
