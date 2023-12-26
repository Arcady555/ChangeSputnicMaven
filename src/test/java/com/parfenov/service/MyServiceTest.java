package com.parfenov.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parfenov.model.CurrencyModel;
import com.parfenov.model.ExchangeModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class MyServiceTest {

    public class MyTestService extends MyService {

        @Override
        public List<CurrencyModel> findAllRegardingOne(String code) throws JsonProcessingException {
            var text = cutList(restCall.get());
            var mapper = new ObjectMapper();
            return mapper.readValue(text, new TypeReference<>() {
            });
        }

        @Override
        public ExchangeModel pairConversion(String codeBase, String codeTarget) throws JsonProcessingException {
            var text = cutPair(restCall.get());
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

    @MockBean
    private RestCall restCall;

    private MyTestService service;
    private CurrencyModel currencyModel1;
    private CurrencyModel currencyModel2;


    @BeforeEach
    public void initService() {
        service = new MyTestService();
        currencyModel1 = new CurrencyModel("RUB", 1);
        currencyModel2 = new CurrencyModel("AED", 0.03990251);
    }

    @Test
    void findAllRegardingOne() throws JsonProcessingException {
        CurrencyModel currencyModel3 = new CurrencyModel("AFN", 0.76833495);
        CurrencyModel currencyModel4 = new CurrencyModel("ZMW", 0.27644080);
        CurrencyModel currencyModel5 = new CurrencyModel("ZWL", 65.51724138);
        when(restCall.get()).thenReturn(
                        """
                                {
                                    "result": "success",
                                    "documentation": "https://www.exchangerate-api.com/docs",
                                    "terms_of_use": "https://www.exchangerate-api.com/terms",
                                    "time_last_update_unix": 1703523602,
                                    "time_last_update_utc": "Mon, 25 Dec 2023 17:00:02 +0000",
                                    "time_next_update_unix": 1703527202,
                                    "time_next_update_utc": "Mon, 25 Dec 2023 18:00:02 +0000",
                                    "base_code": "RUB",
                                    "conversion_rates": 
                                        "RUB": 1,
                                        "AED": 0.03990251,
                                        "AFN": 0.76833495,
                                        "ZMW": 0.27644080,
                                        "ZWL": 65.51724138
                                    }
                                }
                                            """
        );
        List<CurrencyModel> list = service.findAllRegardingOne(currencyModel1.getCode());
        assertArrayEquals(list.toArray(), List.of(
                currencyModel1,
                currencyModel2,
                currencyModel3,
                currencyModel4,
                currencyModel5
        ).toArray());
    }

    @Test
    void pairConversion() throws JsonProcessingException {
        when(restCall.get()).thenReturn(
                        """
                                {
                                    "result": "success",
                                    "documentation": "https://www.exchangerate-api.com/docs",
                                    "terms_of_use": "https://www.exchangerate-api.com/terms",
                                    "time_last_update_unix": 1703588401,
                                    "time_last_update_utc": "Tue, 26 Dec 2023 11:00:01 +0000",
                                    "time_next_update_unix": 1703592001,
                                    "time_next_update_utc": "Tue, 26 Dec 2023 12:00:01 +0000",
                                    "base_code": "RUB",
                                    "target_code": "AED",
                                    "conversion_rate": 0.03985176
                                }
                                            """
        );
        ExchangeModel exchangeModel = new ExchangeModel("RUB", "AED", 0.03985176);
        assertEquals(exchangeModel, service.pairConversion(currencyModel1.getCode(), currencyModel2.getCode()));
    }
}