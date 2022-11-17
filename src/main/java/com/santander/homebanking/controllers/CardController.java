package com.santander.homebanking.controllers;
import com.santander.homebanking.models.CardColor;
import com.santander.homebanking.models.CardType;
import com.santander.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/api")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping(value = "/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardColor cardColor, @RequestParam CardType cardType){
        ArrayList<Object> response=cardService.newCardForClient(authentication,cardColor,cardType);

        return new ResponseEntity<>(response.get(1),HttpStatus.valueOf((Integer)response.get(2)));
    }

    @DeleteMapping(value ="/cards/delete/{id}")
    public ResponseEntity<Object> deleteCard(@PathVariable Long id) {

       ArrayList<Object> response=cardService.deleteCard( id);
       return new ResponseEntity<>(response.get(1),HttpStatus.valueOf((Integer)response.get(2)));
    }
}
