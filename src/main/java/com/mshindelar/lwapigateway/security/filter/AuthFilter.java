package com.mshindelar.lwapigateway.security.filter;

import com.mshindelar.lwapigateway.configuration.AuthFilterConfig;
import com.mshindelar.lwapigateway.exception.MissingAuthCredentialsException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private AuthFilterConfig authFilterConfig;

    private final WebClient.Builder webClientBuilder;

    private final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new MissingAuthCredentialsException("No authentication credentials were provided with the request.");
            }
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] tokens = authHeader.split(" ");

            if(tokens.length != 2 || !"Bearer".equals(tokens[0])) {
                throw new RuntimeException("Incorrect auth structure");
            }

            return webClientBuilder.build()
                    .get()
                    .uri(this.authFilterConfig.getUri() + "/auth/token/validate?token=" + tokens[1])
                    .retrieve().bodyToMono(String.class)
                    .map(id -> {
                        logger.info(id);
                        exchange.getRequest()
                                .mutate()
                                .header("x-auth-header", id);
                        return exchange;
                    }).flatMap(chain::filter);
        });

    }

    public static class Config { }
}
