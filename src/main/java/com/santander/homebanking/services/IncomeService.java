package com.santander.homebanking.services;

import com.santander.homebanking.dtos.LongTermIncomeDTO;
import com.santander.homebanking.models.Account;
import com.santander.homebanking.models.LongTermIncome;
import com.santander.homebanking.models.PeriodType;
import com.santander.homebanking.models.TransactionType;
import com.santander.homebanking.repositories.AccountRepository;
import com.santander.homebanking.repositories.DailyIncomeRepository;
import com.santander.homebanking.repositories.LongTermIncomeRepository;
import com.santander.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncomeService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    LongTermIncomeRepository longTermIncomeRepository;
    @Autowired
    TransactionService transactionService;

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
        transactionService.transactionIncome( TransactionType.DEBIT, -amount, "Solicitud de plazo fijo", account);
        return "accepted";
    }

}

//    public String generateLongIncome(LongTermIncomeDTO longTermIncomeDTO ){
//
//        Account account = accountRepository.findById(id).orElse(null);
//        LongTermIncome longTermIncome = new LongTermIncome(account,amount,period);
//        generatorIncome(longTermIncomeDTO);
//        /*
//         *   Generar plazo fijo
//         *   Transaccion de origin account _ DEBIT
//         *   */
//
//        return "";
//    }
