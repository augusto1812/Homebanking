package com.santander.homebanking.controllers;

import com.santander.homebanking.dtos.ClientDTO;
import com.santander.homebanking.dtos.CurrencyDTO;
import com.santander.homebanking.services.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@RestController
@RequestMapping(value = "/api")
public class CryptoController {

    @Autowired
    CryptoService cryptoService;

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
        ArrayList<Object> response = cryptoService.getPrice(currBuy,currSale);

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

        ArrayList<Object> response = cryptoService.buySaleCurr(
                authentication,
                amount,
                currBuy,
                currSale,
                fromAccountNumber,
                toAccountNumber);

        return new ResponseEntity<>((String)response.get(1),HttpStatus.valueOf((Integer)response.get(2)));

    }


}
