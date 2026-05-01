package com.jobtracker.application.service.impl;

import com.jobtracker.application.exception.InvalidDataException;
import com.jobtracker.application.exception.NotFoundException;
import com.jobtracker.application.mapper.ApplicationMapper;
import com.jobtracker.application.model.entity.Application;
import com.jobtracker.application.model.entity.Company;
import com.jobtracker.application.model.entity.User;
import com.jobtracker.application.model.enums.Status;
import com.jobtracker.application.model.request.ApplicationRequest;
import com.jobtracker.application.model.response.ApplicationResponse;
import com.jobtracker.application.repositories.ApplicationRepository;
import com.jobtracker.application.repositories.CompanyRepository;
import com.jobtracker.application.repositories.UserRepository;
import com.jobtracker.application.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final ApplicationMapper applicationMapper;
    private final CompanyRepository companyRepository;

    @Override
    public ApplicationResponse addApplication(ApplicationRequest request,
                                              Long companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company Not Found"));

        Application application = applicationMapper.toEntity(request);
        application.setCompany(company);
        applicationRepository.save(application);
        return applicationMapper.toResponse(application);
    }

    @Override
    public ApplicationResponse updateApplication(ApplicationRequest request, Long companyId, Long applicationId) {
        companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company Not Found"));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application Not Found"));

        if (!application.getCompany().getId().equals(companyId)) {
            throw new InvalidDataException("Application does not belong to this company");
        }

        if(!application.getStatus().canTransitionTo(Status.valueOf(request.status()))){
            throw new InvalidDataException("Application Status can't be transitioned to " + request.status());
        }

        applicationMapper.updateApplication(request, application);
        applicationRepository.save(application);

        return applicationMapper.toResponse(application);
    }


    @Override
    public void deleteApplication(Long companyId,Long  applicationId) {
        companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company Not Found"));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application Not Found"));

        if (!application.getCompany().getId().equals(companyId)) {
            throw new InvalidDataException("Application does not belong to this company");
        }

        applicationRepository.deleteById(applicationId);
    }

    @Override
    public Page<ApplicationResponse> getAllApplications(Pageable pageable) {
        User user = getCurrentUser();

        return applicationRepository.findAllByCompanyUserId(user.getId(), pageable).map(applicationMapper::toResponse);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

}
