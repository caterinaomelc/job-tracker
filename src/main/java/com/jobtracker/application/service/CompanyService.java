package com.jobtracker.application.service;

import com.jobtracker.application.model.request.CompanyRequest;
import com.jobtracker.application.model.response.CompanyResponse;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    CompanyResponse addCompany(CompanyRequest companyRequest);

    CompanyResponse updateCompany(CompanyRequest companyRequest, Long id);

    void deleteCompany(Long id);

    List<CompanyResponse> getAllCompanies();

    CompanyResponse getCompanyById(Long companyId);

}
