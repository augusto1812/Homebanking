package com.santander.homebanking.controllers;

import com.santander.homebanking.dtos.ClientDTO;


import com.santander.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value="/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClients();
    }

    @GetMapping(value="/clients/{id}")
    public ClientDTO getClient(@PathVariable(name="id") Long id) {
        return clientService.getClient(id);
    }

    @GetMapping("/clients/current")
    public ClientDTO getClientAuth(Authentication authentication) {
        return clientService.getClientAuth(authentication);
    }

    @PostMapping(path = "/clients")
    public ResponseEntity<Object> signUp(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password){
        return clientService.signUp(firstName,lastName,email,password);
    }

}
