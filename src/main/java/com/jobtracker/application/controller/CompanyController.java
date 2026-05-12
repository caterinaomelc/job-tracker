package com.jobtracker.application.controller;

import com.jobtracker.application.model.request.CompanyRequest;
import com.jobtracker.application.model.response.CompanyResponse;
import com.jobtracker.application.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.points.companies}")
public class CompanyController {

    private final CompanyService companyService;


    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CompanyRequest request) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Company {} is created by user {} ", request.name(), currentUserName);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.addCompany(request));

    }

    @GetMapping
    public ResponseEntity<?> getAllCompanies(Pageable pageable) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Companies are getting by user {} ", currentUserName);
        return ResponseEntity.ok(companyService.getAllCompanies(pageable));
    }

    @GetMapping("${end.points.id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Company {} is getting by user {} ", id, currentUserName);
        return ResponseEntity.ok().body(companyService.getCompanyById(id));
    }

    @PutMapping("${end.points.id}")
    public ResponseEntity<CompanyResponse> updateCompany(@Valid @RequestBody CompanyRequest request,
                                                         @PathVariable Long id) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Company {} is updating by user {} ", id, currentUserName);
        return ResponseEntity.ok().body(companyService.updateCompany(request, id));
    }

    @DeleteMapping("${end.points.id}")
    public ResponseEntity<CompanyResponse> deleteCompany(@PathVariable Long id) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Company {} is deleting by user {} ", id, currentUserName);
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }


}
