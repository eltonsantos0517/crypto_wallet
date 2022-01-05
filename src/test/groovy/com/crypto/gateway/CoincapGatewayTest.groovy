package com.crypto.gateway

import com.crypto.usecase.CurrentPrice
import org.junit.Test
import spock.lang.Specification


class CoincapGatewayTest extends Specification {

    private CoincapClient client = Mock()
    private CoincapGateway gateway = new CoincapGateway(client)

    def "Should throw PriceNotFoundException when client return empty or null asset"() {

        given: "a invalid asset symbol"
        String symbol = UUID.randomUUID().toString()

        and: "a empty or null response from client"
        1 * client.getAssetsBySymbol(_) >> {
            String arg ->
                assert arg == symbol

                return AssetDataResponse.builder().data(clientResponse).build()
        }

        when: "get price by symbol is called"
        gateway.getPriceBySymbol(symbol)

        then: "PriceNotFoundException was thrown"
        thrown(PriceNotFoundException.class)

        where:
        clientResponse << [null, Collections.<AssetDataResponse.AssetResponse> emptyList()]
    }

    def "Should throw PriceNotFoundException when client return empty, null asset history"() {

        given: "a valid asset symbol"
        String symbol = "BTC"

        and: "a empty or null response from client"
        String assetId = UUID.randomUUID().toString()
        AssetDataResponse clientResponse = AssetDataResponse.builder()
                .data(
                        Collections.singletonList(
                                AssetDataResponse.AssetResponse.builder()
                                        .symbol(symbol)
                                        .id(assetId)
                                        .build()
                        )
                )
                .build()
        1 * client.getAssetsBySymbol(_) >> {
            String arg ->
                assert arg == symbol

                return clientResponse
        }

        and: "a empty or null asset history response from client"
        1 * client.getAssetHistory(_, _) >> {
            String arg1, String arg2 ->
                assert arg1 == assetId &&
                        arg2 == "d1"

                return AssetHistoryResponse.builder()
                        .data(historyResponse)
                        .build()
        }

        when: "get price by symbol is called"
        gateway.getPriceBySymbol(symbol)

        then: "PriceNotFoundException was thrown"
        thrown(PriceNotFoundException.class)

        where:
        historyResponse << [null, Collections.<AssetHistoryResponse.AssetResponse> emptyList()]
    }

    def "Should throw PriceNotFoundException when client return missing symbol"() {

        given: "a valid asset symbol"
        String symbol = "BTC"

        and: "a empty or null response from client"
        String assetId = UUID.randomUUID().toString()
        AssetDataResponse clientResponse = AssetDataResponse.builder()
                .data(
                        Collections.singletonList(
                                AssetDataResponse.AssetResponse.builder()
                                        .symbol(UUID.randomUUID().toString())
                                        .id(assetId)
                                        .build()
                        )
                )
                .build()
        1 * client.getAssetsBySymbol(_) >> {
            String arg ->
                assert arg == symbol

                return clientResponse
        }

        when: "get price by symbol is called"
        gateway.getPriceBySymbol(symbol)

        then: "PriceNotFoundException was thrown"
        thrown(PriceNotFoundException.class)
    }

    def "Should return last price successful"() {

        given: "a valid asset symbol"
        String symbol = "BTC"

        and: "a empty or null response from client"
        String assetId = UUID.randomUUID().toString()
        AssetDataResponse clientResponse = AssetDataResponse.builder()
                .data(
                        Collections.singletonList(
                                AssetDataResponse.AssetResponse.builder()
                                        .symbol(symbol)
                                        .id(assetId)
                                        .build()
                        )
                )
                .build()
        1 * client.getAssetsBySymbol(_) >> {
            String arg ->
                assert arg == symbol

                return clientResponse
        }

        and: "a empty or null asset history response from client"
        AssetHistoryResponse response = AssetHistoryResponse.builder()
                .data(
                        Arrays.asList(
                                AssetHistoryResponse.AssetResponse.builder()
                                        .priceUsd(new Random().nextDouble())
                                        .time(new Random().nextLong())
                                        .date(UUID.randomUUID().toString())
                                        .build(),
                                AssetHistoryResponse.AssetResponse.builder()
                                        .priceUsd(new Random().nextDouble())
                                        .time(new Random().nextLong())
                                        .date(UUID.randomUUID().toString())
                                        .build(),
                                AssetHistoryResponse.AssetResponse.builder()
                                        .priceUsd(new Random().nextDouble())
                                        .time(new Random().nextLong())
                                        .date(UUID.randomUUID().toString())
                                        .build()
                        )
                )
                .build()
        1 * client.getAssetHistory(_, _) >> {
            String arg1, String arg2 ->
                assert arg1 == assetId &&
                        arg2 == "d1"

                return response
        }

        when: "get price by symbol is called"
        CurrentPrice currentPrice = gateway.getPriceBySymbol(symbol)

        then: "Last price was returned"
        currentPrice.symbol == symbol
        currentPrice.price == response.data.get(2).priceUsd
    }
}
