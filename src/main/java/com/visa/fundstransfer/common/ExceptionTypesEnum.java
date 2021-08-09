package com.visa.fundstransfer.common;

import lombok.Getter;


@Getter
public enum ExceptionTypesEnum
{
    ACCOUNT_MAX_DAILY_TRANSFERS_EXCEEDED("00424","ACCOUNT_MAX_DAILY_TRANSFERS_EXCEEDED"),
    ACCOUNT_NEGATIVE_BALANCE("00425","ACCOUNT_NEGATIVE_BALANCE"),
    ACCOUNT_INSUFFICIENT_FUNDS("00426","ACCOUNT_INSUFFICIENT_FUNDS"),
    ACCOUNT_NOT_FOUND("00427","ACCOUNT_NOT_FOUND"),
    TRANSFER_CURRENCY_NOT_SUPPORTED("00428","TRANSFER_CURRENCY_NOT_SUPPORTED"),
    TRANSFER_INVALID_AMOUNT("00429","TRANSFER_INVALID_AMOUNT"),
    GENERAL_ERROR("01","GENERAL_ERROR"),
    SEVERAL_ERROR("02","SEVERAL_ERROR"),
    EMPTY_PARAMETERS("03","EMPTY_PARAMETERS");

    private String code;
    private String description;
  

    ExceptionTypesEnum(String code,String description)
    {
        this.code          = code;
        this.description   = description;
    }
}
