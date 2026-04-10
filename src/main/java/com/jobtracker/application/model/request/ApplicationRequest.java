package com.jobtracker.application.model.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ApplicationRequest
        (@NotBlank String position,
         BigDecimal salary,
         String notes,
         LocalDate appliedDate,
         @NotBlank String status
        ){
}
