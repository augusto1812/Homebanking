package com.santander.homebanking;

import com.santander.homebanking.dtos.ClientDTO;
import com.santander.homebanking.models.Client;
import com.santander.homebanking.repositories.ClientRepository;
import com.santander.homebanking.services.ClientService;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestJUnit extends TestCase {

    ClientRepository repository = mock(ClientRepository.class);
    ClientService service = new ClientService(repository);

    public void testFindByEmail(){
        //simulación de verificación del repositorio
        List<Client> data = Arrays.asList( new Client(),new Client("pepe","ddd","dsjk@fff.com", "123"));
        Optional<Client> op = Optional.of(data.get(1));
        when(repository.findByEmail("dsjk@fff.com")).thenReturn(op);
        Optional<Client> client = service.getClientByEmail("dsjk@fff.com");
        assertTrue( client.isPresent());
        assertEquals("dsjk@fff.com", client.get().getEmail());
    }
    public void testFindAllClients(){
        List<Client> data = Arrays.asList( new Client(),new Client("pepe","ddd","dsjk@fff.com", "123"));
        when(repository.findAll()).thenReturn(data);
        List<ClientDTO> list = service.getClients();
        assertFalse(list.isEmpty());
    }

    public static Test suit(){
        TestSuite testSuite = new TestSuite( TestJUnit.class );
        return testSuite;
    }

    public static void main(String args[]){
        junit.textui.TestRunner.run(suit());
    }
}
