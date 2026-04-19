package com.jobtracker.application.exception;

public class CompanyAlreadyExists extends RuntimeException {
    public CompanyAlreadyExists(String message) {
        super(message);
    }
}
