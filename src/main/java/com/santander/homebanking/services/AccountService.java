package com.santander.homebanking.services;

import com.santander.homebanking.dtos.AccountDTO;
import com.santander.homebanking.models.Account;
import com.santander.homebanking.models.AccountType;
import com.santander.homebanking.models.CurrencyType;
import com.santander.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MessageSource message;

    public String  getMensaje (String mensaje)
    {
        return   message.getMessage(mensaje,null, LocaleContextHolder.getLocale());

    }

    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    public AccountDTO getAccount(Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    public List<AccountDTO> getAllAccountsByClientId(Long id){
        return accountRepository.getAllAccountsByClientId(id).stream().map(AccountDTO::new).collect(toList());
    }

    public Account newAccount(AccountType accountType, CurrencyType currencyType){
        Random random = new Random();
        String number = "VIN001";
        while ( accountRepository.findByNumber(number).orElse(null) != null) {
            Integer randomI  = random.nextInt(999999 - 1) +1;
            number = String.format("VIN%0"+ 8 + "d",randomI);
        }
        return new Account(number, 0, null, accountType, currencyType);
    }

    public List<AccountDTO> getCurrentClientAccounts(Authentication authentication){
        return  accountRepository.findAccountsByClientEmail(authentication.getName()).stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    public ArrayList<Object> getTypesAccount(){
        AccountType[] response = AccountType.values();
        return new ArrayList<>(Arrays.asList(0,response,200));
    }


}
