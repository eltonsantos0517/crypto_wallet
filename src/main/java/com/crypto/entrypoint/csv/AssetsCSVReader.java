package com.crypto.entrypoint.csv;

import com.crypto.usecase.Asset;
import com.crypto.usecase.GetWalletPerformance;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AssetsCSVReader {

    private final GetWalletPerformance getWalletPerformance;


    public WalletPerformanceResponse read() throws CsvValidationException, IOException, InterruptedException {

        List<Asset> assets = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(new ClassPathResource("crypto.csv").getInputStream()))) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                assets.add(WalletPerformanceConverter.toDomain(values));
            }
        }

        return WalletPerformanceConverter.toResponse(
                getWalletPerformance.execute(assets)
        );
    }
}
