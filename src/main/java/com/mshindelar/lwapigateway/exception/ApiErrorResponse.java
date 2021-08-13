package com.mshindelar.lwapigateway.exception;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiErrorResponse {

    //http status code
    private HttpStatus error;

    // in case we want to provide API based custom error code
    private String status;

    // customer error message to the client API
    private String message;

    // Any furthur details which can help client API
    private String exception;

    // Time of the error.make sure to define a standard time zone to avoid any confusion.
    private LocalDateTime timestamp;

    // getter and setters
    //Builder
    public static final class ApiErrorResponseBuilder {
        private HttpStatus error;
        private String status;
        private String message;
        private String exception;
        private LocalDateTime timestamp;

        public ApiErrorResponseBuilder() {}

        public static ApiErrorResponseBuilder anApiErrorResponse() {
            return new ApiErrorResponseBuilder();
        }

        public ApiErrorResponseBuilder withError(HttpStatus error) {
            this.error = error;
            return this;
        }

        public ApiErrorResponseBuilder withStatus(String status) {
            this.status = status;
            return this;
        }

        public ApiErrorResponseBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ApiErrorResponseBuilder withException(String exception) {
            this.exception = exception;
            return this;
        }

        public ApiErrorResponseBuilder atTime(LocalDateTime timeStamp) {
            this.timestamp = timeStamp;
            return this;
        }
        public ApiErrorResponse build() {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
            apiErrorResponse.error = this.error;
            apiErrorResponse.status = this.status;
            apiErrorResponse.exception = this.exception;
            apiErrorResponse.message = this.message;
            apiErrorResponse.timestamp = this.timestamp;
            return apiErrorResponse;
        }
    }
}