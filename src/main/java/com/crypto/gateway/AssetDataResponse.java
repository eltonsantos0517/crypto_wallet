package com.crypto.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class AssetDataResponse {

    private List<AssetResponse> data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class AssetResponse {
        private String id;
        private String symbol;
    }
}
