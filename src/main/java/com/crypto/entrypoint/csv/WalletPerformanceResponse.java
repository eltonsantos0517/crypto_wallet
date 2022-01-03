package com.crypto.entrypoint.csv;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WalletPerformanceResponse {

    private double total;
    private String bestAsset;
    private double bestPerformance;
    private String worstAsset;
    private double worstPerformance;

    @Override
    public String toString() {
        return "total=" + this.total +
                ",best_asset=" + this.bestAsset +
                ",best_performance=" + this.bestPerformance +
                ",worst_asset=" + this.worstAsset +
                ",worst_performance=" + this.worstPerformance;
    }
}
