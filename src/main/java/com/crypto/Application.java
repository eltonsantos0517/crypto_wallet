package com.crypto;

import com.crypto.entrypoint.csv.AssetsCSVReader;
import com.crypto.entrypoint.csv.WalletPerformanceResponse;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
@EnableFeignClients
public class Application {

    public static void main(String[] args) throws CsvValidationException, IOException, InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        AssetsCSVReader reader = context.getBean(AssetsCSVReader.class);
        WalletPerformanceResponse response = reader.read(new InputStreamReader(new ClassPathResource("crypto.csv").getInputStream()));
        System.out.println(response.toString());
        SpringApplication.exit(context);
    }
}
