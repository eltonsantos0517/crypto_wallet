package com.crypto.entrypoint.csv;

import com.crypto.usecase.Asset;
import com.crypto.usecase.GetWalletPerformance;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AssetsCSVReader {

    private final GetWalletPerformance getWalletPerformance;


    public WalletPerformanceResponse read(Reader reader) throws CsvValidationException, IOException, InterruptedException {

        List<Asset> assets = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(reader)) {
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
