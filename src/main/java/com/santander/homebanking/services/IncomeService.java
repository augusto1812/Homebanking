package com.santander.homebanking.services;

import com.santander.homebanking.dtos.LongTermIncomeDTO;
import com.santander.homebanking.models.*;
import com.santander.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncomeService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    LongTermIncomeRepository longTermIncomeRepository;
    @Autowired
    TransactionService transactionService;
    @Autowired
    ClientRepository clientRepository;

    public String generatorIncome(LongTermIncomeDTO longTermIncomeDTO){
       Account account =accountRepository.findById(longTermIncomeDTO.getAccountId()).orElse(null);
       Double amount= longTermIncomeDTO.getAmount();
       PeriodType periodType=longTermIncomeDTO.getPeriodType();

        if (account == null || amount.isNaN() || periodType == null){
            return "general.missingData";
        }

        if( accountRepository.findById( account.getId()).orElse(null) == null){
          return "transaction.createTransaction.missingAccountFrom";
        }

        LongTermIncome income = new LongTermIncome(account,amount,periodType);
        longTermIncomeRepository.save(income);
        transactionService.transactionIncome( TransactionType.DEBIT, -amount, "Solicitud de plazo fijo. N°: "+ income.getId()+ " ("+ income.getPeriod().getDays()+" days)", account);
        return "accepted";
    }

    public List<LongTermIncomeDTO> getAllLongIncomes(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName()).orElse(null);
        List<Account> accounts = client.getAccounts().stream().collect(Collectors.toList());
        List<LongTermIncomeDTO> incomes = new ArrayList<>();
        accounts.forEach(account -> {
            account.getLongTermIncomes().forEach(LongTerm ->
            {
                incomes.add(new LongTermIncomeDTO(LongTerm));

            });


        });

        return incomes;
    }

}


