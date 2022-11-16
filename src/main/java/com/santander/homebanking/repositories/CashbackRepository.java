package com.santander.homebanking.repositories;

import com.santander.homebanking.models.CardColor;
import com.santander.homebanking.models.CardType;
import com.santander.homebanking.models.Cashback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface CashbackRepository  extends JpaRepository<Cashback,Long> {

    Optional<Cashback> findByCardColorAndCardType(CardColor cardColor, CardType cardType);

}
