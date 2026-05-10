package com.jobtracker.application.exception;

import java.time.LocalDateTime;

public record ErrorResponse (
    int status,
    String message,
    String errorCode,
    LocalDateTime timestamp

) {}
