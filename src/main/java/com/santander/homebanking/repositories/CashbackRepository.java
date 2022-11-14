package com.santander.homebanking.repositories;

import com.santander.homebanking.models.Cashback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CashbackRepository  extends JpaRepository<Cashback,Long> {

}
