package com.parfenov.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parfenov.model.CurrencyModel;
import com.parfenov.model.ExchangeModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MyService {
    @Value("${service.url}")
    private String url;

    @Value("${service.key}")
    private String key;

    public List<CurrencyModel> findAllRegardingOne(String code) throws JsonProcessingException {
        String urlRC = url + key + "/latest/" + code;
        var text = cutList(new RestCall(urlRC).get());
        var mapper = new ObjectMapper();
        return mapper.readValue(text, new TypeReference<>() {
        });
    }

    public ExchangeModel pairConversion(String codeBase, String codeTarget) throws JsonProcessingException {
        String urlRC = url + key + "/pair/" + codeBase + "/" + codeTarget;
        var text = cutPair(new RestCall(urlRC).get());
        var mapper = new ObjectMapper();
        return mapper.readValue(text, new TypeReference<>() {
        });
    }

    private String cutList(String str) {
        String[] array1 = str.split("\"conversion_rates\":");
        String rsl = "[" + array1[1].replaceAll(",", "},{\"code\"=")
                .replaceAll(":", ",\"rate\":")
                .replaceAll("=", ":");
        return "[{\"code\":" + rsl.substring(2, rsl.length() - 2) + "]";
    }

    private String cutPair(String str) {
        String[] array1 = str.split("\"base_code\"");
        return  "{ \"base_code\"" + array1[1];
    }
}