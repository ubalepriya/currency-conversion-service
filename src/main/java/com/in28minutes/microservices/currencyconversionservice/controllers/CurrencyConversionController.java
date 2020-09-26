package com.in28minutes.microservices.currencyconversionservice.controllers;

import com.in28minutes.microservices.currencyconversionservice.feignproxy.CurrencyExchangeProxy;
import com.in28minutes.microservices.currencyconversionservice.models.CurrencyConversionBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    private final CurrencyExchangeProxy currencyExchangeProxy;

    public CurrencyConversionController(CurrencyExchangeProxy currencyExchangeProxy) {
        this.currencyExchangeProxy = currencyExchangeProxy;
    }

    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean getCurrencyValue(@PathVariable String from, @PathVariable String to,
                                                   @PathVariable BigDecimal quantity)
    {
        RestTemplate restTemplate                   =   new RestTemplate();
        Map<String,String> uriVariables             =   new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        ResponseEntity<CurrencyConversionBean> responseEntity
                =    restTemplate.getForEntity("http://localhost:8001/exchange/from/{from}/to/{to}", CurrencyConversionBean.class,uriVariables );
        CurrencyConversionBean currencyConversionBean   =   responseEntity.getBody();

        return new CurrencyConversionBean(currencyConversionBean.getId(),
                from,
                to,
                currencyConversionBean.getConversionMultiple(),
                quantity,
                quantity.multiply(currencyConversionBean.getConversionMultiple()),
                currencyConversionBean.getPort());
    }

    @GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean getCurrencyValueFeign(@PathVariable String from, @PathVariable String to,
                                                   @PathVariable BigDecimal quantity)
    {
        Map<String,String> uriVariables             =   new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        CurrencyConversionBean currencyConversionBean   =    currencyExchangeProxy.getCurrencyExchangeValue(from,to);

        return new CurrencyConversionBean(currencyConversionBean.getId(),
                from,
                to,
                currencyConversionBean.getConversionMultiple(),
                quantity,
                quantity.multiply(currencyConversionBean.getConversionMultiple()),
                currencyConversionBean.getPort());
    }
}
