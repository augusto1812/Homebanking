package com.santander.homebanking.repositories;

import com.santander.homebanking.models.LongTermIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface LongTermIncomeRepository extends JpaRepository<LongTermIncome,Long> {

    List<LongTermIncomeRepository> findByAccount(Long id);
}
