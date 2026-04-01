package com.jobtracker.application.repositories;

import com.jobtracker.application.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
