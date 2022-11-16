package com.santander.homebanking.services;

import com.santander.homebanking.dtos.CashbackDTO;
import com.santander.homebanking.dtos.DiscountDTO;
import com.santander.homebanking.repositories.CashbackRepository;
import com.santander.homebanking.repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CashbackService {

    @Autowired
    CashbackRepository cashbackRepository;

    public List<CashbackDTO> getCashbacks() {
        return cashbackRepository.findAll().stream().map(CashbackDTO::new).collect(toList());
    }
    public CashbackDTO getCashback(Long id) {
        return cashbackRepository.findById(id).map(CashbackDTO::new).orElse(null);
    }
}
