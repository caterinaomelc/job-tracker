package com.jobtracker.application.controller;

import com.jobtracker.application.model.enums.Status;
import com.jobtracker.application.model.request.ApplicationRequest;
import com.jobtracker.application.model.response.ApplicationResponse;
import com.jobtracker.application.service.ApplicationService;
import com.jobtracker.application.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("${end.points.companies.id.applications}")
    public ResponseEntity<ApplicationResponse> addApplication(@PathVariable Long companyId,
                                                              @Valid @RequestBody ApplicationRequest applicationRequest) {

        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} adding an application", currentUserName);
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.addApplication(applicationRequest, companyId));
    }

    @GetMapping("${end.points.applications}")
    public ResponseEntity<Page<ApplicationResponse>> getApplications(Pageable pageable,
                                                                     @RequestParam(name = "status", required = false) Status status) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} getting applications", currentUserName);
        return ResponseEntity.ok(applicationService.getAllApplications(pageable, status));

    }

    @PutMapping("${end.points.companies.id.applications.id}")
    public ResponseEntity<ApplicationResponse> updateApplication(@PathVariable Long companyId,
                                                                 @PathVariable("id") Long applicationId,
                                                                 @Valid @RequestBody ApplicationRequest applicationRequest) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} updating an application for company {}", currentUserName, companyId);
        return ResponseEntity.ok(applicationService.updateApplication(applicationRequest, companyId, applicationId));
    }

    @DeleteMapping("${end.points.companies.id.applications.id}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Long companyId,
            @PathVariable("id") Long applicationId) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} deleting an application for company {}", currentUserName, companyId);
        applicationService.deleteApplication(companyId, applicationId);
        return ResponseEntity.noContent().build();

    }

}
