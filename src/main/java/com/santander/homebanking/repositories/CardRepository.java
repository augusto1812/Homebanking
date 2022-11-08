package com.santander.homebanking.repositories;

import com.santander.homebanking.models.Card;
import com.santander.homebanking.models.CardType;
import com.santander.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CardRepository extends JpaRepository<Card,Long> {
    List<Card> findByCvvOrderByCvvDesc(int cvv);
    Optional<Card> findByNumber(String number);
    List<Card> findByTypeAndClient(CardType type, Client client);
    Set<Card> findByActiveTrue();


}
