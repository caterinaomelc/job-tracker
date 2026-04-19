package com.jobtracker.application.model.response;

import java.util.List;

public record CompanyResponse(
        Long id,
        String name,
        String address,
        String email,
        String website,
        String notes,
        List<ApplicationResponse> applications
) { }
