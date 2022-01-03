package com.crypto.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class AssetHistoryResponse {

    private List<AssetResponse> data;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class AssetResponse {
        private double priceUsd;
        private long time;
        private String date;
    }
}
