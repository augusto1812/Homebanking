package com.santander.homebanking.repositories;

import com.santander.homebanking.dtos.TransactionDTO;
import com.santander.homebanking.models.Loan;
import com.santander.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByDateGreaterThan(LocalDateTime date);
}
