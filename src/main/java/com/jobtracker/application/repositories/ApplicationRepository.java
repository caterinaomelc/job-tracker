package com.jobtracker.application.repositories;

import com.jobtracker.application.model.entity.Application;
import com.jobtracker.application.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

    Optional<List<Application>> findApplicationByCompany(Company company);
}
