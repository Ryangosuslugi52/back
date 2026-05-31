package com.example.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import java.util.Map;

@Singleton
public class GlobalExceptionHandler implements ExceptionHandler<Exception, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, Exception exception) {
        if (exception instanceof IllegalArgumentException || exception instanceof NullPointerException) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Bad Request",
                            "message", "Неверный формат входных данных: " + exception.getMessage()
                    ));
        }

        return HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "Internal Server Error",
                        "message", exception.getMessage()
                ));
    }
}