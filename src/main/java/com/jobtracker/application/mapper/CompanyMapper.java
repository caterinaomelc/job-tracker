package com.jobtracker.application.mapper;

import com.jobtracker.application.model.entity.Company;
import com.jobtracker.application.model.request.CompanyRequest;
import com.jobtracker.application.model.response.CompanyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyResponse toResponse(Company company);

    @Mapping(target = "applications", ignore = true)
    Company toEntity(CompanyRequest companyRequest);

    @Mapping(target = "applications", ignore = true)
    Company updateEntity(CompanyRequest request, @MappingTarget Company company);

}