package com.jobtracker.application.controller;

import com.jobtracker.application.model.request.ApplicationRequest;
import com.jobtracker.application.model.response.ApplicationResponse;
import com.jobtracker.application.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("${end.points.companies.id.applications}")
    public ResponseEntity<ApplicationResponse> addApplication(@PathVariable Long companyId,
                                                              @Valid @RequestBody ApplicationRequest applicationRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.addApplication(applicationRequest, companyId));
    }

    @GetMapping("${end.points.applications}")
    public ResponseEntity<List<ApplicationResponse>> getApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @PutMapping("${end.points.companies.id.applications.id}")
    public ResponseEntity<ApplicationResponse> updateApplication(@PathVariable Long companyId,
                                                                 @PathVariable("id") Long applicationId,
                                                                 @Valid @RequestBody ApplicationRequest applicationRequest) {
        return ResponseEntity.ok(applicationService.updateApplication(applicationRequest, companyId, applicationId));
    }

    @DeleteMapping("${end.points.companies.id.applications.id}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Long companyId,
            @PathVariable("id") Long applicationId) {
        applicationService.deleteApplication(companyId, applicationId);
        return ResponseEntity.noContent().build();

    }

}
