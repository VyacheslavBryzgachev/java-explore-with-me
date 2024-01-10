package ru.practicum.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private final String error;
    private final String message;
    private final String reason;
    private final HttpStatus status;
    private final LocalDateTime timestamp;

    public ApiError(String error, String message, String reason, HttpStatus status, LocalDateTime timestamp) {
        this.error = error;
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
    }
}
