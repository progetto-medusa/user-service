package com.progettomedusa.user_service.config;

import okhttp3.ConnectionSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OkHttpClientCustom {

    @Bean
    public okhttp3.OkHttpClient okHttpClient(){
        return new okhttp3.OkHttpClient.Builder().connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS,ConnectionSpec.COMPATIBLE_TLS,ConnectionSpec.CLEARTEXT)).build().newBuilder().build();
    }
}