package com.mshindelar.lwapigateway;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.netty.http.client.HttpClient;

@SpringBootApplication
public class LWApiGatewayApplication {

	public static void main(String[] args) { SpringApplication.run(LWApiGatewayApplication.class, args); }

}
