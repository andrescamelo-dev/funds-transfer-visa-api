package com.visa.fundstransfer.exception;

import com.visa.fundstransfer.common.ExceptionTypesEnum;

public class FundsTransferException extends DefaultFundsTransferException {

    private static final long serialVersionUID = 1L;
    private ExceptionTypesEnum exceptionTypesEnum;

    public FundsTransferException(ExceptionTypesEnum exceptionTypesEnum){
        this.exceptionTypesEnum = exceptionTypesEnum;
    }

    public ExceptionTypesEnum getExceptionTypesEnum() {
        return exceptionTypesEnum;
    }


}