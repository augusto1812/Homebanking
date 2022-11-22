package com.santander.homebanking;

import com.santander.homebanking.dtos.ClientDTO;
import com.santander.homebanking.models.Client;
import com.santander.homebanking.repositories.ClientRepository;
import com.santander.homebanking.services.ClientService;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientServiceTest  {

    ClientRepository repositoryCli = mock(ClientRepository.class);
    ClientService serviceCli = new ClientService(repositoryCli);
    List<Client> dataClients = Arrays.asList(
            new Client(),
            new Client("pepe","ddd","dsjk@fff.com", "123")
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
}
