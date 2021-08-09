package com.visa.fundstransfer.service;

import java.util.Map;

import com.google.common.base.Strings;
import com.visa.fundstransfer.common.ExceptionTypesEnum;
import com.visa.fundstransfer.domain.dto.RateExchangeDTO;
import com.visa.fundstransfer.exception.FundsTransferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RateExchangeService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Value("${funds-transfer.rate-exchange.uri:http://api.exchangeratesapi.io/v1/latest}")
    private String fundsTransferRateExchangeUri;

    @Value("${funds-transfer.rate-exchange.access-key}")
    private String fundsTransferRateExchangeAccessKey;

    @Value("${funds-transfer.rate-exchange.currency.locale}")
    private String fundsTransferRateExchangeCurrencyLocale;

    @Autowired
    RestTemplate restTemplate;

    @Cacheable(cacheNames = "rates")
    public Double getLocaleRateExchange(String originCurrency, String stDate) {
        RateExchangeDTO rateExchange = getLatestCurrencyRates(originCurrency);
        Map<String, Double> mapRates = rateExchange.getRates();
        Double localeRate = mapRates.get(fundsTransferRateExchangeCurrencyLocale);
        Double originRate = mapRates.get(originCurrency);
        Double localRateExchange = 1.0;

        if (originRate > 0) {
            localRateExchange = localeRate / originRate;
        }
        return localRateExchange;
    }

    public RateExchangeDTO getLatestCurrencyRates(String stOriginCurrency) throws FundsTransferException {
        String stRateExchangeUri = fundsTransferRateExchangeUri + "?symbols=" + fundsTransferRateExchangeCurrencyLocale;
        if (!Strings.isNullOrEmpty(stOriginCurrency)) {
            stRateExchangeUri += "," + stOriginCurrency;
        } else {
            throw new FundsTransferException(ExceptionTypesEnum.TRANSFER_CURRENCY_NOT_SUPPORTED);
        }
        logger.info("Getting latest rates (exchange uri: {} )", stRateExchangeUri);

        stRateExchangeUri += "&access_key=" + fundsTransferRateExchangeAccessKey;
        ResponseEntity<RateExchangeDTO> response = restTemplate.getForEntity(stRateExchangeUri, RateExchangeDTO.class);
        RateExchangeDTO rateExchange = response.getBody();
        return rateExchange;
    }
}
