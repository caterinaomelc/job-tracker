package com.jobtracker.application.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @NotBlank String username,

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$", message = "Password must be at least 8 chars with letters and digits")
        @NotBlank String password,

        @NotBlank String email
) {}
