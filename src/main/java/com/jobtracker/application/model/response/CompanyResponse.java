package com.jobtracker.application.model.response;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CompanyResponse(
        String name,
        String address,
        String email,
        String website,
        String notes,
        List<ApplicationResponse> applications
) { }
