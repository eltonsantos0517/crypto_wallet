package com.crypto.usecase;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CurrentPrice {

    private String symbol;
    private double price;
}
