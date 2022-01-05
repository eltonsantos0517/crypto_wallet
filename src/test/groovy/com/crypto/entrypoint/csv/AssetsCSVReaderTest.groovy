package com.crypto.entrypoint.csv

import com.crypto.usecase.Asset
import com.crypto.usecase.GetWalletPerformance
import com.crypto.usecase.WalletPerformance
import org.junit.Test
import spock.lang.Specification

class AssetsCSVReaderTest extends Specification {

    private GetWalletPerformance useCase = Mock()
    private AssetsCSVReader reader = new AssetsCSVReader(useCase)

    def "Should read input stream reader and return wallet performance successful"() {
        given: "a valid input stream reader"
        String symbol = "BTC"
        double quantity = 0.12345
        double price = 37870.5058
        StringReader inputStreamReader = new StringReader(symbol + "," + quantity + "," + price)

        and: "a valid response from use case"
        WalletPerformance performance = WalletPerformance.builder()
                .worstAsset(UUID.randomUUID().toString())
                .bestAsset(UUID.randomUUID().toString())
                .bestPerformance(new Random().nextDouble())
                .worstPerformance(new Random().nextDouble())
                .total(new Random().nextDouble())
                .build()
        1 * useCase.execute(_) >> {
            List<List<Asset>> args ->
                assert args.get(0).size() == 1 &&
                        args.get(0).get(0).symbol == symbol &&
                        args.get(0).get(0).quantity == quantity &&
                        args.get(0).get(0).price == price

                return performance
        }

        when: "reader is called"
        WalletPerformanceResponse response = reader.read(inputStreamReader);

        then: "the wallet response are the same returned by use case"
        response.bestAsset == performance.bestAsset
        response.bestPerformance == performance.bestPerformance
        response.worstAsset == performance.worstAsset
        response.worstPerformance == performance.worstPerformance
        response.total == performance.total
    }
}
