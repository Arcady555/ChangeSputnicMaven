package com.parfenov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyModel {
    private String code;
    private double rate;

    public String getName() {
        try {
            return Currency.getInstance(code).getDisplayName();
        } catch (IllegalArgumentException e) {
            return " ";
        }
    }
}