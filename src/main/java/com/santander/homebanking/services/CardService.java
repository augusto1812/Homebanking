package com.santander.homebanking.services;

import com.santander.homebanking.models.Card;
import com.santander.homebanking.models.CardColor;
import com.santander.homebanking.models.CardType;
import com.santander.homebanking.models.Client;
import com.santander.homebanking.repositories.CardRepository;
import com.santander.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

import static com.santander.homebanking.utils.CardUtils.getCardNumber;
import static com.santander.homebanking.utils.CardUtils.getCvv;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private HttpSession session;
    @Autowired
    private MessageService messageService;



    LocalDateTime today = LocalDateTime.now();
    LocalDateTime todayPlus5Years = today.plusYears(5) ;

    public ArrayList<Object> newCardForClient(Authentication authentication, CardColor cardColor, CardType cardType){
        //obtener info cliente authenticado
        Client client = clientRepository.findByEmail(authentication.getName()).orElse(null);
        //validacion si tiene 3 tarjetas del mismo tipo
        if(client == null){

            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("card.newCardForClient.clientNotFound"), 403));
        }

        if (cardRepository.findByTypeAndClient(cardType,client).size()>= 3){

            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("card.newCardForClient.3Cards"), 403));
        }

        Random random  = new Random();

        String number = getCardNumber();


        int cvv = getCvv();

        Card card = new Card(client.getFirstName()+" "+client.getLastName(),cardType,cardColor,number,cvv,today,todayPlus5Years,client,true);
//        Set<Card> cards = new HashSet<>();
//        cards.add(card);
//        client.setCards(new HashSet<>(cards));
        client.getCards().add(card);
        card.setClient(client);
        cardRepository.save(card);


        return new ArrayList<>(Arrays.asList(0,messageService.getMessage("card.newCardForClient.created"), 201));

    }

    public ArrayList<Object> deleteCard(Long id) {
        Client client = (Client) session.getAttribute("client");

        Card card = client.getCards().stream().filter(cardAux -> cardAux.getId() == id).findFirst().orElse(null);

        if (card == null) {
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("card.deleteCard.cardDoesntExists"), 403));

        }

        card.setActive(false);

        cardRepository.save(card);
        return new ArrayList<>(Arrays.asList(0,messageService.getMessage("card.deleteCard.inactiveCard"), 200));

    }


}
