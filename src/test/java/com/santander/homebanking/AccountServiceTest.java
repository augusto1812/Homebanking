package com.santander.homebanking;

import com.santander.homebanking.models.Account;
import com.santander.homebanking.models.AccountType;
import com.santander.homebanking.models.Client;
import com.santander.homebanking.models.CurrencyType;
import com.santander.homebanking.repositories.AccountRepository;
import com.santander.homebanking.repositories.ClientRepository;
import com.santander.homebanking.services.AccountService;
import com.santander.homebanking.services.ClientService;
import com.santander.homebanking.services.MessageService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    ClientRepository repositoryCli = mock(ClientRepository.class);
    AccountRepository repositoryAcc = mock(AccountRepository.class);
    AccountService serviceAcc = new AccountService(repositoryAcc);
    MessageService msgService = new MessageService( mock(MessageSource.class) );
    ClientService serviceCli = new ClientService(repositoryCli,repositoryAcc,serviceAcc, msgService);

    /*   DATA   */
    List<Client> dataClients = Arrays.asList(
            new Client(),
            new Client("melba","morel","melba@minhub.com", "123")
    );
    List<Account> dataAccounts = Arrays.asList(
            serviceAcc.newAccount(AccountType.CA, CurrencyType.ARS));

    @Test
    public void testFindAccountByNumber(){
        Optional<Account> accountOp = Optional.of(dataAccounts.get(0));
        when(repositoryAcc.findByNumber(dataAccounts.get(0).getNumber())).thenReturn(accountOp);
        Optional<Account> accountServ = serviceAcc.getAccountByNumber(dataAccounts.get(0).getNumber());
        Assert.assertEquals( accountOp,accountServ);
    }
    @Test
    public void testNewAccountForCurrentClient(){
        Optional<Client> op = Optional.of(dataClients.get(1));
        when(repositoryCli.findByEmail("melba@minhub.com")).thenReturn(op);
        ArrayList<Object> newAccount = serviceCli.newAccountToClient(op.get().getEmail(),AccountType.CA,CurrencyType.ARS);
        Assert.assertEquals(201, newAccount.get(2));
        //when(msgService.getMessage(newAccount.get(1).toString())).thenReturn();
    }
}
