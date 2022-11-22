package com.santander.homebanking.controllers;

import com.santander.homebanking.dtos.ClientCurrentDTO;
import com.santander.homebanking.dtos.ClientDTO;


import com.santander.homebanking.dtos.ClientUpdateDTO;
import com.santander.homebanking.dtos.ShopDTO;
import com.santander.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/clients/current/profile")
    public ClientCurrentDTO getClientCurrentDTO(Authentication authentication){
        return clientService.getCurrentClient(authentication);
    }


    @PostMapping(path = "/clients")
    public ResponseEntity<Object> signUp(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password){
       ArrayList<Object> response=clientService.signUp(firstName,lastName,email,password);
        return new ResponseEntity<>(response.get(1), HttpStatus.valueOf((Integer)response.get(2)));
    }

    @PostMapping(value = "/clients/current/profile")
    public ResponseEntity<Object> updateProfile(Authentication authentication,@RequestBody ClientUpdateDTO clientUpdateDTO){
        ArrayList<Object> response = clientService.updateDataClient(authentication.getName(),clientUpdateDTO);
        return new ResponseEntity<>(response.get(1), HttpStatus.valueOf((Integer)response.get(2)));
    }
}
