package com.crypto.usecase;

public class UnknownPriceException extends RuntimeException{

    public UnknownPriceException(Throwable cause) {
        super(cause);
    }
}
