package com.crypto.usecase;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class WalletPerformance {

    private double total;
    private String bestAsset;
    private double bestPerformance;
    private String worstAsset;
    private double worstPerformance;
}
