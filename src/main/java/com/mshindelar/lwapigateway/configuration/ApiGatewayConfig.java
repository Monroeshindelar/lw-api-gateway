package com.mshindelar.lwapigateway.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="application.gateway-settings")
@Getter
@Setter
public class ApiGatewayConfig {
    private String allowedOrigin;
}
