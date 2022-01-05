package com.crypto.gateway;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class AssetDataResponse {

    private List<AssetResponse> data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class AssetResponse {
        private String id;
        private String symbol;
    }
}
