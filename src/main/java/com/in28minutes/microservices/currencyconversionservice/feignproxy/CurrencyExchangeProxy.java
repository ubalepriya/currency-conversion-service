package com.in28minutes.microservices.currencyconversionservice.feignproxy;

import com.in28minutes.microservices.currencyconversionservice.models.CurrencyConversionBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency-exchange-service", url = "http://localhost:8001")
public interface CurrencyExchangeProxy {

    @GetMapping("/exchange/from/{from}/to/{to}")
    public CurrencyConversionBean getCurrencyExchangeValue(@PathVariable String from, @PathVariable String to);

}
