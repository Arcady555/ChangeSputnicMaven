package com.parfenov.controller;

import com.parfenov.model.CurrencyModel;
import com.parfenov.model.ExchangeModel;
import com.parfenov.service.MyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyService service;

    private CurrencyModel currencyModel1;
    private CurrencyModel currencyModel2;

    @BeforeEach
    public void initService() {
        currencyModel1 = new CurrencyModel("RUB", 1);
        currencyModel2 = new CurrencyModel("AED", 0.03990251);
    }

    @Test
    void whenFindAll() throws Exception {
        CurrencyModel currencyModel3 = new CurrencyModel("AFN", 0.76833495);
        CurrencyModel currencyModel4 = new CurrencyModel("ZMW", 0.27644080);
        CurrencyModel currencyModel5 = new CurrencyModel("ZWL", 65.51724138);
        when(service.findAllRegardingOne("RUB")).thenReturn(List.of(
                currencyModel1,
                currencyModel2,
                currencyModel3,
                currencyModel4,
                currencyModel5
                )
        );
        mockMvc.perform(get("/allCurrencies/RUB")
                .sessionAttr("list", service.findAllRegardingOne("RUB")))
                .andDo(print())
                .andExpect(model().attribute("list", service.findAllRegardingOne("RUB")))
                .andExpect(status().isOk())
                .andExpect(view().name("all"));
    }

    @Test
    void pairExchange() throws Exception {
        ExchangeModel exchangeModel = new ExchangeModel("RUB", "AED", 0.03985176);
        when(service.pairConversion("RUB", "AED")).thenReturn(exchangeModel);
        mockMvc.perform(get("/pair_exchange/RUB/AED")
                        .sessionAttr("pairExchange", service.pairConversion("RUB", "AED")))
                .andDo(print())
                .andExpect(model().attribute("pairExchange", service.pairConversion("RUB", "AED")))
                .andExpect(status().isOk())
                .andExpect(view().name("pair"));
    }
}