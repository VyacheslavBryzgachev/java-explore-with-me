package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return new ApiError(HttpStatus.NOT_FOUND.name(), exception.getMessage(), "Not Found", HttpStatus.NOT_FOUND, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleWrongRequestException(final WrongRequestException exception) {
        log.error(exception.getMessage(), exception);
        return new ApiError(HttpStatus.CONFLICT.name(), exception.getMessage(), "Conflict", HttpStatus.CONFLICT, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleWrongTimeException(final WrongTimeException exception) {
        log.error(exception.getMessage(), exception);
        return new ApiError(HttpStatus.BAD_REQUEST.name(), exception.getMessage(), "Bad request", HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(final ConstraintViolationException exception) {
        log.error(exception.getMessage(), exception);
        return new ApiError(HttpStatus.BAD_REQUEST.name(), exception.getMessage(), "Bad request", HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }
}
