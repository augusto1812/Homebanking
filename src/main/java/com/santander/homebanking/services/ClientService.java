package com.santander.homebanking.services;

import com.santander.homebanking.dtos.ClientDTO;
import com.santander.homebanking.models.Account;
import com.santander.homebanking.models.AccountType;
import com.santander.homebanking.models.Client;
import com.santander.homebanking.models.CurrencyType;
import com.santander.homebanking.repositories.AccountRepository;
import com.santander.homebanking.repositories.ClientRepository;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;



    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    public ClientDTO getClient(Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }
    public ClientDTO getClientAuth(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName()).map(ClientDTO::new).orElse(null);
    }

    public ResponseEntity<Object> signUp(String firstName, String lastName,
                                         String email, String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(messageService.getMessage("cliente.signUp.missingData"), HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email).orElse(null) !=  null) {
            return new ResponseEntity<>(messageService.getMessage("cliente.signUp.emailFound"), HttpStatus.FORBIDDEN);
        }
        Client newClient = clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
        Account account = accountService.newAccount(AccountType.CA,CurrencyType.ARS);
        account.setClient(newClient);
        accountRepository.save(account);

        return new ResponseEntity<>(messageService.getMessage("cliente.signUp.created"),HttpStatus.CREATED);
    }

    public ResponseEntity<Object> newAccountToClient(Authentication authentication, AccountType accountType, CurrencyType currencyType){

        Client client = clientRepository.findByEmail(authentication.getName()).orElse(null);
        if (client.getAccounts().size()>=3) {
            String message3Accounts = messageService.getMessage("cliente.newAccountToClient.3accounts");
            return new ResponseEntity<>(message3Accounts,HttpStatus.FORBIDDEN);
        }
        Account account = accountService.newAccount(accountType, currencyType);
        account.setClient(client);
        accountRepository.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
