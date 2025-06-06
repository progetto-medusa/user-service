package com.progettomedusa.user_service.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties("mail-service")
public class MailServiceProperties {
    private String url;
}


