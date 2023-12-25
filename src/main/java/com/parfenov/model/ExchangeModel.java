package com.parfenov.model;

public record ExchangeModel(String base_code, String target_code, double conversion_rate) {
}