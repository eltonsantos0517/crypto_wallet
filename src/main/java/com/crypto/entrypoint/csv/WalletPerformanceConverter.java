package com.crypto.entrypoint.csv;

import com.crypto.usecase.Asset;
import com.crypto.usecase.WalletPerformance;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WalletPerformanceConverter {

    static Asset toDomain(String[] values) {
        return Asset.builder()
                .symbol(values[0])
                .quantity(Double.parseDouble(values[1]))
                .price(Double.parseDouble(values[2]))
                .build();
    }

    static WalletPerformanceResponse toResponse(WalletPerformance domain) {
        return WalletPerformanceResponse.builder()
                .total(domain.getTotal())
                .bestPerformance(domain.getBestPerformance())
                .bestAsset(domain.getBestAsset())
                .worstPerformance(domain.getWorstPerformance())
                .worstAsset(domain.getWorstAsset())
                .build();
    }


}
