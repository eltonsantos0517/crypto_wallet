package com.crypto.gateway;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
class AssetHistoryResponse {

    private List<AssetResponse> data;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    static class AssetResponse {
        private double priceUsd;
        private long time;
        private String date;
    }
}
