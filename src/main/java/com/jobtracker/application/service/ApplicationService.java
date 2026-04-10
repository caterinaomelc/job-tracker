package com.jobtracker.application.service;

import com.jobtracker.application.model.request.ApplicationRequest;
import com.jobtracker.application.model.response.ApplicationResponse;

import java.util.List;

public interface ApplicationService {

    ApplicationResponse addApplication(ApplicationRequest request, Long companyId);

    ApplicationResponse updateApplication(ApplicationRequest request, Long companyId, Long applicationId);

    void deleteApplication(Long companyId, Long applicationId);

    List<ApplicationResponse> getAllApplications();
}
