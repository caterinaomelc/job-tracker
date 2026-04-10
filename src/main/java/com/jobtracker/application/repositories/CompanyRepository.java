package com.jobtracker.application.repositories;

import com.jobtracker.application.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findAllByUserId(Long userId);

    Optional<Company> findByName(String name);


}
