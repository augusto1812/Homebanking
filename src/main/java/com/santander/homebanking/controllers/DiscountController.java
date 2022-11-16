package com.santander.homebanking.controllers;

import com.santander.homebanking.dtos.ClientDTO;
import com.santander.homebanking.dtos.DiscountDTO;
import com.santander.homebanking.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    @GetMapping(value="/discounts")
    public List<DiscountDTO> getDiscounts() {
        return discountService.getDiscounts();
    }

    @GetMapping(value="/discounts/{id}")
    public DiscountDTO getDiscounts(@PathVariable(name="id") Long id) {
        return discountService.getDiscount(id);
    }
}
