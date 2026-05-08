package com.jobtracker.application.service;

import com.jobtracker.application.model.enums.Status;
import com.jobtracker.application.model.request.ApplicationRequest;
import com.jobtracker.application.model.response.ApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ApplicationService {

    ApplicationResponse addApplication(ApplicationRequest request, Long companyId);

    ApplicationResponse updateApplication(ApplicationRequest request, Long companyId, Long applicationId);

    void deleteApplication(Long companyId, Long applicationId);

    Page<ApplicationResponse> getAllApplications(Pageable pageable, Status status);
}
