package com.santander.homebanking.repositories;

import com.santander.homebanking.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DiscountRepository  extends JpaRepository<Discount,Long> {
}
