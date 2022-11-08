package com.santander.homebanking.controllers;
import com.santander.homebanking.models.CardColor;
import com.santander.homebanking.models.CardType;
import com.santander.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping(value = "/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardColor cardColor, @RequestParam CardType cardType){
        return cardService.newCardForClient(authentication,cardColor,cardType);

    }

    @DeleteMapping(value ="/cards/delete/{id}")
    public ResponseEntity<Object> deleteCard(@PathVariable Long id) {
        return  cardService.deleteCard( id);
    }
}
