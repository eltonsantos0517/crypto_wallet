package com.crypto.usecase

import com.crypto.gateway.AssetGateway
import spock.lang.Specification

class GetWalletPerformanceTest extends Specification {

    private AssetGateway gateway = Mock()
    private GetWalletPerformance getWalletPerformance = new GetWalletPerformance(gateway)

    def setup() {
        getWalletPerformance.maxConcurrentThreads = 1
        getWalletPerformance.decimalPlaces = 2

    }

    def "Should return wallet performance successful"() {
        given: "valid assets"
        Asset btc = Asset.builder()
                .symbol("BTC")
                .quantity(0.12345)
                .price(37870.5058)
                .build()
        Asset eth = Asset.builder()
                .symbol("ETH")
                .quantity(4.89532)
                .price(2003.9774)
                .build()

        and: "valid current prices from gateway"
        CurrentPrice btcPrice = CurrentPrice.builder()
                .symbol("BTC")
                .price(56999.9728252053067291)
                .build()

        CurrentPrice ethPrice = CurrentPrice.builder()
                .symbol("ETH")
                .price(2032.1394325557042107)
                .build()

        2 * gateway.getPriceBySymbol(_) >> {
            String arg ->
                assert arg == btc.symbol

                return btcPrice
        } >> {
            String arg ->
                assert arg == eth.symbol

                return ethPrice
        }

        when: "the get performance wallet is called"
        WalletPerformance walletPerformance = getWalletPerformance.execute(Arrays.asList(btc, eth))

        then: "the wallet performance was returned with correctly data"
        walletPerformance.total == 16984.62D
        walletPerformance.bestAsset == "BTC"
        walletPerformance.bestPerformance == 1.51D
        walletPerformance.worstAsset == "ETH"
        walletPerformance.worstPerformance == 1.01D
    }
}
