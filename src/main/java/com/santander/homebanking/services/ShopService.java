package com.santander.homebanking.services;

import com.santander.homebanking.dtos.ShopDTO;
import com.santander.homebanking.models.*;
import com.santander.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service
public class ShopService {

    @Autowired
    MessageService messageService;

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    CashbackRepository cashbackRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;


    public ArrayList<Object> shop(ShopDTO shopDTO) {
        Long cardId = shopDTO.getCardId();
        SectorType sectorType = shopDTO.getSectorType();
        Double amount = shopDTO.getAmount();

        Card card = cardRepository.findById(cardId).orElse(null);

        Account account = card.getAccount();

        //Valido si alguno de los parametros llega vacio
        if (card == null || sectorType == null || amount.isNaN()) {
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("shop.missingParameters"),403));
            }

            //Valido si la tarjeta esta activa
        if(!card.isActive()){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("shop.cardNotActive"),403));
        }
            //Valido si el sector exise

            //Valido si el monto es valido
        if(amount <= 0) {
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("shop.invalidAmount"),403));
        }

        //Valido si puedo realizar la compra
        if(amount > account.getBalance()) {
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("shop.insufficientMoney"),403));
        }

            //Busco por sector y luego por tarjeta el descuento apropiado
        Discount discount = discountRepository.findBySectorType(sectorType).orElse(null);
        if(discount != null) {
            Double discountPercentage = discount.getPercentagePerCard().get(card.getColor());
            Double amountDiscounted = amount * ((100 - discountPercentage)/100);
            String descriptionDiscount = "Compra realizada por: " + amount + ". Descuento aplicado: " + discountPercentage;

            //Creo transaccion debit con descuento aplicado
            account.setBalance(account.getBalance() - amountDiscounted);

            Transaction transactionDiscount = new Transaction(TransactionType.DEBIT,amountDiscounted,descriptionDiscount,account);
            transactionRepository.save(transactionDiscount);
        } else {
            String descriptionWithoutDiscount = "Compra realizada por: " + amount + ".";

            account.setBalance(account.getBalance() - amount);

            Transaction transactionWithoutDiscount = new Transaction(TransactionType.DEBIT,amount,descriptionWithoutDiscount,account);
            transactionRepository.save(transactionWithoutDiscount);
        }


        //Busco cashback por tarjeta
        Cashback cashback =  cashbackRepository.findByCardColorAndCardType(card.getColor(),card.getType()).orElse(null);
        Double cashbackPercentage = cashback.getPercentage();
        Double amountCashback = amount * cashbackPercentage;
        String descriptionCashback = "Por compra: " + amount + ". Cashback acreditado: " + amountCashback;

        //Creo transaccion credit con cashback aplicado
        account.setBalance(account.getBalance() + amountCashback);

        Transaction transactionCashback = new Transaction(TransactionType.CREDIT,amountCashback,descriptionCashback,account);
        transactionRepository.save(transactionCashback);


        return new ArrayList<>(Arrays.asList(0,messageService.getMessage("shop.ok"),200));
        }
    }
