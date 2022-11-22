package com.santander.homebanking;

import com.santander.homebanking.dtos.ClientDTO;
import com.santander.homebanking.dtos.ClientUpdateDTO;
import com.santander.homebanking.models.Client;
import com.santander.homebanking.repositories.AccountRepository;
import com.santander.homebanking.repositories.ClientRepository;
import com.santander.homebanking.services.AccountService;
import com.santander.homebanking.services.ClientService;
import com.santander.homebanking.services.MessageService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientServiceTest {

    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    AccountRepository repositoryAcc = mock(AccountRepository.class);
    AccountService serviceAcc = new AccountService(repositoryAcc);
    MessageService msgService = new MessageService( mock(MessageSource.class) );
    ClientRepository repositoryCli = mock(ClientRepository.class);
    ClientService serviceCli = new ClientService(repositoryCli,repositoryAcc,serviceAcc, msgService,passwordEncoder);

    List<Client> dataClients = Arrays.asList(
            new Client(),
            new Client("pepe","ddd","dsjk@fff.com", "{bcrypt}$2a$10$to9SmOXwTvb5wLjEyWaY/usLGZ8YoV5rLqVUM.07QE/ZRU.DFhFgK")
    );

    @Test
    public void testFindByEmail(){
        Optional<Client> op = Optional.of(dataClients.get(1));
        when(repositoryCli.findByEmail("dsjk@fff.com")).thenReturn(op);
        Optional<Client> client = serviceCli.getClientByEmail("dsjk@fff.com");
        Assert.assertTrue( client.isPresent());
        Assert.assertEquals("dsjk@fff.com", client.get().getEmail());
    }

    @Test
    public void testFindAllClients(){
        when(repositoryCli.findAll()).thenReturn(dataClients);
        List<ClientDTO> list = serviceCli.getClients();
        Assert.assertFalse(list.isEmpty());
    }

    @Test
    public void testUpdateClient(){
        Optional<Client> op = Optional.of(dataClients.get(1));
        when(repositoryCli.findByEmail("dsjk@fff.com")).thenReturn(op);
        when(passwordEncoder.matches("123", op.get().getPassword())).thenReturn(true);
        when(passwordEncoder.encode("333")).thenReturn("{bcrypt}$2a$10$to9SmOXwTvb5wLjEyWaY/usLG");
        ClientUpdateDTO clientUpdateDTO = new ClientUpdateDTO("123","333","Calle falsa 123","+54343254545");
        ArrayList<Object> updateClient = serviceCli.updateDataClient("dsjk@fff.com", clientUpdateDTO);
        Assert.assertEquals(201, updateClient.get(2));
    }
}
