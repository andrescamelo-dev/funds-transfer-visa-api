package com.visa.fundstransfer.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DefaultFundsTransferException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DefaultFundsTransferException(String message){
        super(message);
    }

    public DefaultFundsTransferException(String message, Throwable exception){
        super(message,exception);
    }


}