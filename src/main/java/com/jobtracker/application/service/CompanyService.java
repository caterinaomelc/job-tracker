package com.jobtracker.application.service;

import com.jobtracker.application.model.request.CompanyRequest;
import com.jobtracker.application.model.response.CompanyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CompanyService {

    CompanyResponse addCompany(CompanyRequest companyRequest);

    CompanyResponse updateCompany(CompanyRequest companyRequest, Long id);

    void deleteCompany(Long id);


    Page<CompanyResponse> getAllCompanies(Pageable pageable);

    CompanyResponse getCompanyById(Long companyId);

}
