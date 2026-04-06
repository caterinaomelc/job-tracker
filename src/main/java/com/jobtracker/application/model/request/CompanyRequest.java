package com.jobtracker.application.model.request;

import jakarta.validation.constraints.NotBlank;

public record CompanyRequest(
        @NotBlank String name,
        String address,
        String email,
        String website,
        String notes
) { }
