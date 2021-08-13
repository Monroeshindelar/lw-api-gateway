package com.mshindelar.lwapigateway.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class CustomErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public CustomErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
        super(errorAttributes, resourceProperties, applicationContext);
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions
                .route(RequestPredicates.all(), request -> {
                   Throwable error = errorAttributes.getError(request);

                   ApiErrorResponse apiErrorResponse = new ApiErrorResponse
                           .ApiErrorResponseBuilder()
                           .withException(error.toString())
                           .withMessage(error.toString())
                           .withStatus("" + HttpStatus.INTERNAL_SERVER_ERROR.value())
                           .withError(HttpStatus.INTERNAL_SERVER_ERROR)
                           .atTime(LocalDateTime.now(ZoneOffset.UTC))
                           .build();

                   if(error instanceof MissingAuthCredentialsException) {
                       apiErrorResponse.setStatus("" + HttpStatus.UNAUTHORIZED.value());
                       apiErrorResponse.setError(HttpStatus.UNAUTHORIZED);
                   }

                   return ServerResponse.status(Integer.valueOf(apiErrorResponse.getStatus())).syncBody(apiErrorResponse)
                           .subscriberContext((Context) request.attribute("context").orElse(Context.empty()));
                });
    }
}
