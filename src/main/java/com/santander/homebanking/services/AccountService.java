package com.santander.homebanking.services;

import com.santander.homebanking.dtos.AccountDTO;
import com.santander.homebanking.dtos.LongTermIncomeDTO;
import com.santander.homebanking.models.*;
import com.santander.homebanking.repositories.AccountRepository;
import com.santander.homebanking.repositories.DailyIncomeRepository;
import com.santander.homebanking.repositories.LongTermIncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MessageSource message;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private DailyIncomeRepository dailyIncomeRepository;
    @Autowired
    private LongTermIncomeRepository longTermIncomeRepository;



    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    public AccountDTO getAccount(Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    public List<AccountDTO> getAllAccountsByClientId(Long id) {
        return accountRepository.getAllAccountsByClientId(id).stream().map(AccountDTO::new).collect(toList());
    }

    public Account newAccount() {
        Random random = new Random();
        String number = "VIN001";
        while (accountRepository.findByNumber(number).orElse(null) != null) {
            Integer randomI = random.nextInt(999999 - 1) + 1;
            number = String.format("VIN%0" + 8 + "d", randomI);
        }
        return new Account(number, 0, null);
    }

    public List<AccountDTO> getCurrentClientAccounts(Authentication authentication) {
        return accountRepository.findAccountsByClientEmail(authentication.getName()).stream().map(AccountDTO::new).collect(Collectors.toList());
    }


    //second, minute, hour, day of month, month, day(s) of week
    @Scheduled(cron = "10 * * * * *")
    public void addDailyIncome() {

    Set<Account> accounts=accountRepository.findAll().stream().collect(Collectors.toSet());
    accounts.stream().forEach(account ->{
        Double balance=account.getBalance();
        Double interes=balance*0.01;
        DailyIncome dailyIncome= new DailyIncome(account,interes,0.01);
        dailyIncomeRepository.save(dailyIncome);
        transactionService.transactionIncome(TransactionType.CREDIT,interes,"Acreditacion diaria",account);


    } );
    }


    @Scheduled(cron = "10 * * * * *")
    public void addLongTerm(){
        String result = "";
        Set<Account> accounts=accountRepository.findAll().stream().collect(Collectors.toSet());
        accounts.stream().forEach(account ->{
        Set<LongTermIncome> longIncomes = account.getLongTermIncomes();
        for ( LongTermIncome x : longIncomes ){
            if( LocalDateTime.now().isAfter( x.getCreationDate().plusSeconds( x.getPeriod().getDays())) && x.getActive()){
                x.setActive(false);
                longTermIncomeRepository.save(x);
                Double amount = (x.getAmount() * x.getPeriod().getPercentage()) + x.getAmount();
                transactionService.transactionIncome(TransactionType.CREDIT, amount, "Acreditación plazo fijo", account);
            }
        }
    });
    }




}


