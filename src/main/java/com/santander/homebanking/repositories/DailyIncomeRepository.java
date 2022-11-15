package com.santander.homebanking.repositories;

import com.santander.homebanking.models.ClientLoan;
import com.santander.homebanking.models.DailyIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DailyIncomeRepository extends JpaRepository<DailyIncome,Long> {

}
