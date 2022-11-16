package com.santander.homebanking.repositories;

import com.santander.homebanking.models.Account;
import com.santander.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account,Long> {
    List<Account> findByBalanceOrderByNumberAsc(Double balance);
    Optional<Account> findByNumber(String number);
    List<Account> findAccountsByClient(Client client);
    List<Account> findAccountsByClientEmail(String email);
    List<Account> findByBalanceLessThan(Double number);
    @Query("SELECT a FROM Account a WHERE a.id =?1")
    List<Account> getAllAccountsByClientId(Long id);
    Optional<Account> findByClientAndNumber(Client client, String number);

//    @Query("SELECT a FROM Account a WHERE balance = ?1 and number =? 2")
//    List<Account> buscarCuentaPorBalanceYNumero(double balance, String number);
//
//    @Query(value = "SELECT * from ACCOUNT JOIN CLIENT on id = ?1", nativeQuery = true)
//    Optional<Client> obtenerCuentaPorId(Long id);
}
