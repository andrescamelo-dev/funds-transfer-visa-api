package com.visa.fundstransfer.domain.dto;


import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferResponseDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    private String id;
    private String taxCollected;
    private String CAD;
}
