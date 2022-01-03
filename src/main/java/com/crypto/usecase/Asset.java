package com.crypto.usecase;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Asset {
    private String symbol;
    private double quantity;
    private double price;
}
