package com.crypto.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${coincap.url}", name = "coincap-client", configuration = CoincapClientConfiguration.class)
interface CoincapClient {

    @GetMapping(value = "/assets", produces = MediaType.APPLICATION_JSON_VALUE)
    AssetDataResponse getAssetsBySymbol(@RequestParam("search") String symbol);

    @GetMapping(value = "/assets/{id}/history", produces = MediaType.APPLICATION_JSON_VALUE)
    AssetHistoryResponse getAssetHistory(@PathVariable("id") String id,
                                         @RequestParam("interval") String interval);

}
