package com.jobtracker.application.mapper;

import com.jobtracker.application.model.entity.Company;
import com.jobtracker.application.model.request.CompanyRequest;
import com.jobtracker.application.model.response.CompanyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyResponse toResponse(Company company);

    Company toEntity(CompanyRequest companyRequest);

    Company updateEntity(CompanyRequest request, @MappingTarget Company company);

}