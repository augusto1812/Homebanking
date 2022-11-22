package com.santander.homebanking.services;

import com.santander.homebanking.dtos.ClientCurrentDTO;
import com.santander.homebanking.dtos.ClientDTO;
import com.santander.homebanking.dtos.ClientUpdateDTO;
import com.santander.homebanking.models.Account;
import com.santander.homebanking.models.AccountType;
import com.santander.homebanking.models.Client;
import com.santander.homebanking.models.CurrencyType;
import com.santander.homebanking.repositories.AccountRepository;
import com.santander.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class ClientService {

    private ClientRepository clientRepository;
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private AccountService accountService;
    private MessageService messageService;
    @Autowired
    private HttpSession session;

    /* CONSTRUCTORS*/

    public ClientService(ClientRepository clientRepository, AccountRepository accountRepository, AccountService accountService, MessageService messageServ, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.messageService = messageServ;
        this.passwordEncoder = passwordEncoder;
    }

    /*METHODS*/
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    public ClientDTO getClient(Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }
    public ClientCurrentDTO getCurrentClient(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName()).map(ClientCurrentDTO::new).orElse(null);
    }

    public ClientDTO getClientAuth(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName()).map(ClientDTO::new).orElse(null);
    }

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public ArrayList<Object> signUp(String firstName, String lastName,
                                         String email, String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("cliente.signUp.missingData"), 403));

        }
        if (clientRepository.findByEmail(email).orElse(null) !=  null) {
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("cliente.signUp.emailFound"), 403));

        }
        Client newClient = clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
        Account account = accountService.newAccount(AccountType.CA,CurrencyType.ARS);
        account.setClient(newClient);
        accountRepository.save(account);
        return new ArrayList<>(Arrays.asList(0,messageService.getMessage("cliente.signUp.created"), 201));

    }

    public ArrayList<Object> newAccountToClient(String email, AccountType accountType, CurrencyType currencyType){

        Client client = clientRepository.findByEmail(email).orElse(null);

        if(accountType==null||currencyType==null)
        { return new ArrayList<>(Arrays.asList(1,messageService.getMessage("general.missingData"), 403));}

        if(currencyType==CurrencyType.ARS &&client.getAccounts().stream().filter(account -> account.getCurrencyType()==currencyType).count()>=3)
        {
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("cliente.newAccountToClient.3accounts"), 403));
        }
        if(currencyType!=CurrencyType.ARS&&accountType!=AccountType.CA)
        {
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("cliente.newAccountToClient.CA"), 403));
        }
        if(currencyType!=CurrencyType.ARS&&client.getAccounts().stream().filter(account -> account.getCurrencyType()==currencyType).count()>=1)
        {
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("cliente.newAccountToClient.1accounts"), 403));
        }
        Account account = accountService.newAccount(accountType, currencyType);
        account.setClient(client);
        accountRepository.save(account);
        return new ArrayList<>(Arrays.asList(0,messageService.getMessage("cliente.signUp.created"), 201));
    }

    public ArrayList<Object> updateDataClient(String email,ClientUpdateDTO clientUpdateDTO){
        Client client = clientRepository.findByEmail(email).orElse(null);
        Boolean pass = passwordEncoder.matches(clientUpdateDTO.getPassword(), client.getPassword());
        if (!pass){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("cliente.updateData.passWrong"), 403));
        }
        client.setPassword(passwordEncoder.encode(clientUpdateDTO.getNewPassword()));
        client.setAddress(clientUpdateDTO.getAddress());
        client.setPhone(clientUpdateDTO.getPhone());
        clientRepository.save(client);
        return new ArrayList<>(Arrays.asList(0,messageService.getMessage("cliente.updateData.update"), 201));
    }
}
