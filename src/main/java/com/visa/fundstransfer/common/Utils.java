package com.visa.fundstransfer.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visa.fundstransfer.domain.dto.ErrorResponseDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    @Value("${funds-transfer.tax.base-amount}")
    private Double fundsTransferTaxBaseAmount;

    @Value("${funds-transfer.tax.less-percentage}")
    private Double fundsTransferTaxLessPercentage;

    @Value("${funds-transfer.tax.greater-percentage}")
    private Double fundsTransferTaxGreaterPercentage;

    public static String formatMessage(String stSourceMessage, String value) {
        return String.format(stSourceMessage, value);
    }

    public static String formatDate(Date datDate, String stFormat) {

        if (datDate == null)
            return "";

        DateFormat dateFormat = new SimpleDateFormat(stFormat);
        return dateFormat.format(datDate);
    }

    public static String getJSONFormat(Object objSourceObject) {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(objSourceObject);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            json = "";
        }
        return json;
    }

    public static Date convertLocalDateToDate(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static ErrorResponseDTO errorResponse(String code, String description) {
        String[] errors = new String[1];
        errors[0] = description;
        return new ErrorResponseDTO (code,errors);
    }

    public Double calculateLocaleTransferTax(Double transferAmount) {
        Double result = 0.0;
        if (transferAmount != null && transferAmount > 0) {
            if (transferAmount < fundsTransferTaxBaseAmount) {
                result = transferAmount * fundsTransferTaxLessPercentage / 100;
            } else if (transferAmount > fundsTransferTaxBaseAmount)
                result = transferAmount * fundsTransferTaxGreaterPercentage / 100;
        }
        return result;
    }

}
