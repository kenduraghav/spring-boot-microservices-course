package com.example.bookstore.order.web.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.example.bookstore.order.domain.InvalidOrderException;
import com.example.bookstore.order.domain.OrderNotFoundException;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final URI NOT_FOUND_TYPE = URI.create("https://api.bookstore.com/errors/not-found");
    private static final URI INTL_SERVER_ERROR_TYPE =
            URI.create("https://api.bookstore.com/errors/internal-server-error");
    private static final URI BAD_REQUEST_TYPE = URI.create("https://api.bookstore.com/errors/bad-request");
    private static final String SERVICE_NAME = "order-service";

    @ExceptionHandler(Exception.class)
    ProblemDetail handleUnhandledException(Exception ex) {
        log.error("Unhandled exception occurred: ", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                INTERNAL_SERVER_ERROR, "Something went wrong. Please try again later.");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(INTL_SERVER_ERROR_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("error_category", "Generic");
        return problemDetail;
    }

    @ExceptionHandler(OrderNotFoundException.class)
    ProblemDetail handleProductNotFoundException(OrderNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Product Not Found");
        problemDetail.setType(NOT_FOUND_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", "Generic");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(InvalidOrderException.class)
    ProblemDetail handleInvalidOrderException(InvalidOrderException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Invalid Order Creation Request");
        problemDetail.setType(BAD_REQUEST_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", "Generic");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> validationErrors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Validation failed for one or more fields.");

        problemDetail.setTitle("Validation Error");
        problemDetail.setType(BAD_REQUEST_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", "Validation");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errors", validationErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}
