package com.visa.fundstransfer.domain.dto;


import lombok.Data;

@Data
public class ErrorResponseDTO {

    private String errorCode;
    private String[] errors;

    public ErrorResponseDTO(String errorCode, String[] errors){
        this.errorCode = errorCode;
        this.errors = errors;
    }
}
