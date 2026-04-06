package com.jobtracker.application.model.response;

import jakarta.validation.constraints.NotBlank;

public record CompanyResponse(
        String name,
        String address,
        String email,
        String website,
        String notes
) { }
