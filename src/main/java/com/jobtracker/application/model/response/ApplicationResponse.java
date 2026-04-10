package com.jobtracker.application.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ApplicationResponse(
        String position,
        BigDecimal salary,
        String notes,
        LocalDate appliedDate,
        String status,
        LocalDateTime createdAt
) {
}
