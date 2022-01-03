package com.crypto.gateway;

import feign.FeignException;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Slf4j
public class CoincapClientConfiguration {

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, 500, 5);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            if (HttpStatus.TOO_MANY_REQUESTS.value() == response.status()) {
                log.warn("Http status 429 detected. retrying call to url " + response.request().url());
                return new RetryableException(response.status(), "Http status 429 detected. retrying call",
                        response.request().httpMethod(),
                        new Date(),
                        response.request());
            }

            return FeignException.errorStatus(methodKey, response);
        };
    }
}


