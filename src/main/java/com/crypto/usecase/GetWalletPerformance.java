package com.crypto.usecase;

import com.crypto.gateway.AssetGateway;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetWalletPerformance {

    @Value("${max-concurrent-threads}")
    private int maxConcurrentThreads;

    @Value("${decimal-places}")
    private int decimalPlaces;

    private final AssetGateway gateway;

    public WalletPerformance execute(List<Asset> assets) throws InterruptedException {
        List<CurrentPrice> prices = submitAndWaitPriceTasks(assets);
        return consolidateWallet(assets, prices);
    }

    private List<CurrentPrice> submitAndWaitPriceTasks(List<Asset> assets) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(maxConcurrentThreads);

        List<Future<CurrentPrice>> futures = executor.invokeAll(
                assets.stream()
                        .map(asset -> new GetPriceTask(asset.getSymbol()))
                        .collect(Collectors.toList())
        );

        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.MINUTES);

        return futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("Has an error to resolve future", e);
                        throw new UnknownPriceException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private WalletPerformance consolidateWallet(List<Asset> assets, List<CurrentPrice> prices) {
        AtomicReference<String> bestAsset = new AtomicReference<>();
        AtomicReference<Double> bestPerformance = new AtomicReference<>(Double.MIN_VALUE);
        AtomicReference<String> worstAsset = new AtomicReference<>();
        AtomicReference<Double> worstPerformance = new AtomicReference<>(Double.MAX_VALUE);
        AtomicReference<Double> total = new AtomicReference<>(0.0);

        prices.forEach(price -> {
            Asset originalAsset = assets.stream().filter(asset -> asset.getSymbol().equals(price.getSymbol()))
                    .findAny()
                    .get();

            double performance = (price.getPrice() * 100 / originalAsset.getPrice()) / 100;

            if (performance > bestPerformance.get()) {
                bestPerformance.set(performance);
                bestAsset.set(price.getSymbol());
            }

            if (performance < worstPerformance.get()) {
                worstPerformance.set(performance);
                worstAsset.set(price.getSymbol());
            }

            total.set(total.get() + (price.getPrice() * originalAsset.getQuantity()));
        });

        return WalletPerformance.builder()
                .bestAsset(bestAsset.get())
                .worstAsset(worstAsset.get())
                .bestPerformance(BigDecimal.valueOf(bestPerformance.get()).setScale(decimalPlaces, RoundingMode.HALF_UP).doubleValue())
                .worstPerformance(BigDecimal.valueOf(worstPerformance.get()).setScale(decimalPlaces, RoundingMode.HALF_UP).doubleValue())
                .total(BigDecimal.valueOf(total.get()).setScale(decimalPlaces, RoundingMode.HALF_UP).doubleValue())
                .build();
    }

    @AllArgsConstructor
    private class GetPriceTask implements Callable<CurrentPrice> {

        private String symbol;

        @Override
        public CurrentPrice call() throws Exception {
            log.info("Start task [{}, {}]",
                    StructuredArguments.kv("symbol", symbol),
                    StructuredArguments.kv("time", LocalDateTime.now()));
            CurrentPrice price = gateway.getPriceBySymbol(symbol);
            log.info("End task [{}, {}]",
                    StructuredArguments.kv("symbol", symbol),
                    StructuredArguments.kv("time", LocalDateTime.now()));
            return price;
        }
    }
}
