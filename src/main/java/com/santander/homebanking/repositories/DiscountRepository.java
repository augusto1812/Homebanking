package com.santander.homebanking.repositories;

import com.santander.homebanking.models.Discount;
import com.santander.homebanking.models.SectorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface DiscountRepository  extends JpaRepository<Discount,Long> {

    Optional<Discount> findBySectorType(SectorType sectorType);
}
