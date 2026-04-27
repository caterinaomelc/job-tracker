package com.jobtracker.application.repositories;

import com.jobtracker.application.model.entity.Company;
import com.jobtracker.application.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long>{


    Optional<Company> findByName(String name);

    boolean existsByNameAndUser(String name, User user);

    Page<Company> findAllByUserId(Long userId, Pageable pageable);


}
