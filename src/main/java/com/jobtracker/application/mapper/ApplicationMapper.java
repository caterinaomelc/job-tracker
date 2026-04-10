package com.jobtracker.application.mapper;

import com.jobtracker.application.model.entity.Application;
import com.jobtracker.application.model.request.ApplicationRequest;
import com.jobtracker.application.model.response.ApplicationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    ApplicationResponse toResponse(Application application);

    Application toEntity(ApplicationRequest applicationRequest);

    Application updateApplication(ApplicationRequest applicationRequest, @MappingTarget Application application);
}

