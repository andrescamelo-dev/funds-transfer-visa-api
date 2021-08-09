package com.visa.fundstransfer.domain.dto;


import java.io.Serializable;

import lombok.Data;

@Data
public class TransferDTO implements Serializable{

    private static final long serialVersionUID = 1L;
    private Double amount;
    private String currency;
    private Integer originAccount;
    private Integer destinationAccount;
    private String description;

  
}
