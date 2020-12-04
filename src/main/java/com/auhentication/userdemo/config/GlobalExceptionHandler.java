package com.auhentication.userdemo.config;

import com.auhentication.userdemo.payload.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        Integer code = null;
        String msg = null;
        if (ex.getMessage().contains("401")) {
            code = HttpStatus.UNAUTHORIZED.value();
            msg = "Unauthorized";
        }else{
            code = HttpStatus.INTERNAL_SERVER_ERROR.value();
            msg = "Inter server Error";
        }
        ResponseEntity<ApiResponse> entity = ResponseEntity.status(code)
                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .body(

                        ApiResponse.builder()
                                .message ( msg )
                                .status ( code )
                                .build()
                );
        return setHttpResponse(entity, exchange);
    }

    public Mono<Void> setHttpResponse(
            final ResponseEntity<ApiResponse> entity,
            final ServerWebExchange exchange
    ) {
        final ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(entity.getStatusCode());
        response.getHeaders().addAll(entity.getHeaders());
        try {
            final DataBuffer buffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(entity.getBody()));
            return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
        } catch (final JsonProcessingException ex) {
            return Mono.error(ex);
        }
    }


}
