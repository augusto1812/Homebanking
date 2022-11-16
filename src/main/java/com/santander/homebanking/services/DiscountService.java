package com.santander.homebanking.services;

import com.santander.homebanking.dtos.ClientDTO;
import com.santander.homebanking.dtos.DiscountDTO;
import com.santander.homebanking.repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class DiscountService {

    @Autowired
    DiscountRepository discountRepository;

    public List<DiscountDTO> getDiscounts() {
        return discountRepository.findAll().stream().map(DiscountDTO::new).collect(toList());
    }
    public DiscountDTO getDiscount(Long id) {
        return discountRepository.findById(id).map(DiscountDTO::new).orElse(null);
    }
}
