package com.visa.fundstransfer.domain.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.visa.fundstransfer.common.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateExchangeDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    private boolean success;
    private Timestamp timestamp;
    private String base;
    private Date date;
    private Map<String, Double> rates = new HashMap<>();

    @Override
    public String toString() {
        return Utils.getJSONFormat(this);
    }
}
