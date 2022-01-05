package com.crypto.gateway;

import com.crypto.usecase.CurrentPrice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
class CoincapGateway implements AssetGateway {

    private final CoincapClient client;

    public CurrentPrice getPriceBySymbol(String symbol) {

        log.info("Start get price [{}]",
                StructuredArguments.kv("symbol", symbol));
        AssetDataResponse assetsFound = client.getAssetsBySymbol(symbol);

        CurrentPrice price = Optional.ofNullable(assetsFound.getData()).orElseGet(Collections::emptyList).stream()
                .filter(assetFound -> symbol.equals(assetFound.getSymbol()))
                .findAny()
                .map(asset -> client.getAssetHistory(asset.getId(), "d1"))
                .filter(assetHistory -> !CollectionUtils.isEmpty(assetHistory.getData()))
                .map(assetHistory -> CurrentPrice.builder()
                        .symbol(symbol)
                        .price(assetHistory.getData().get(assetHistory.getData().size() - 1).getPriceUsd())
                        .build())
                .orElseThrow(PriceNotFoundException::new);

        log.info("Get price successful [{}, {}]",
                StructuredArguments.kv("symbol", symbol),
                StructuredArguments.kv("current_price", price.getPrice()));

        return price;
    }


}
