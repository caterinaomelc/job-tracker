package com.jobtracker.application.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidData(InvalidDataException e) {
        ErrorResponse error = new ErrorResponse(400, e.getMessage(), "BAD_REQUEST", LocalDateTime.now());
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
            log.error("Attempt by user {} has failed. InvalidDataException: {}", currentUserName, e.getMessage());
        } else {
            log.error("InvalidDataException: ", e);
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        ErrorResponse error = new ErrorResponse(400, e.getMessage(), "BAD_REQUEST", LocalDateTime.now());
        log.error("MethodArgumentNotValidException: ", e);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        ErrorResponse error = new ErrorResponse(404, e.getMessage(), "NOT_FOUND", LocalDateTime.now());
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
            log.error("Attempt by user {} has failed. NotFoundException: {}", currentUserName, e.getMessage());
        } else {
            log.error("NotFoundException: ", e);
        }
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CompanyAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleCompanyAlreadyExists(CompanyAlreadyExists e) {
        ErrorResponse error = new ErrorResponse(409, e.getMessage(), "CONFLICT", LocalDateTime.now());
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
            log.error("Attempt by user {} has failed. CompanyAlreadyExists: {}", currentUserName, e.getMessage());        } else {
            log.error("CompanyAlreadyExists: ", e);
        }

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        ErrorResponse error = new ErrorResponse(500, "Internal serves error", "INTERNAL_SERVER_ERROR", LocalDateTime.now());
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
            log.error("User {} has encountered a server error. Internal serves error", currentUserName);
        } else {
            log.error("Internal serves error ", e);
        }
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
