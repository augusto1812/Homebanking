package com.santander.homebanking.repositories;

import com.santander.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client,Long> {
    List<Client> findByFirstNameOrderByLastNameDesc(String firstName);
    Optional<Client> findByEmail(String email);

}
