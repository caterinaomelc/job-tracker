package com.jobtracker.application.service.impl;

import com.jobtracker.application.exception.CompanyAlreadyExists;
import com.jobtracker.application.exception.InvalidDataException;
import com.jobtracker.application.exception.NotFoundException;
import com.jobtracker.application.mapper.CompanyMapper;
import com.jobtracker.application.model.entity.Company;
import com.jobtracker.application.model.entity.User;
import com.jobtracker.application.model.request.CompanyRequest;
import com.jobtracker.application.model.response.CompanyResponse;
import com.jobtracker.application.repositories.CompanyRepository;
import com.jobtracker.application.repositories.UserRepository;
import com.jobtracker.application.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserRepository userRepository;

    @Override
    public CompanyResponse addCompany(CompanyRequest request) {

        Company company = companyMapper.toEntity(request);
        User currentUser = getCurrentUser();
        company.setUser(currentUser);

        if (companyRepository.existsByNameAndUser(company.getName(), currentUser)) {
            throw new CompanyAlreadyExists("Company already exists");
        }

        companyRepository.save(company);

        return companyMapper.toResponse(company);
    }

    @Override
    public CompanyResponse updateCompany(CompanyRequest request, Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company Not Found"));


        if (!company.getUser().getId().equals(getCurrentUser().getId())) {
            throw new InvalidDataException("Access denied");
        }

        company = companyMapper.updateEntity(request, company);
        companyRepository.save(company);

        return companyMapper.toResponse(company);
    }

    @Override
    public void deleteCompany(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company Not Found"));

        if (!company.getUser().getId().equals(getCurrentUser().getId())) {
            throw new InvalidDataException("Access denied");
        }

        companyRepository.delete(company);

    }


    public Page<CompanyResponse> getAllCompanies(Pageable pageable) {

        return companyRepository
                .findAllByUserId(getCurrentUser().getId(), pageable)
                .map(companyMapper::toResponse);
    }

    public CompanyResponse getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company Not Found"));
        if (!company.getUser().getId().equals(getCurrentUser().getId())) {
            throw new InvalidDataException("Access denied");
        }
        return companyMapper.toResponse(company);
    }


    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
