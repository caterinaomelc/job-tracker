package com.jobtracker.application.repositories;

import com.jobtracker.application.model.entity.Company;
import com.jobtracker.application.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long>{


    Optional<Company> findByName(String name);

    boolean existsByNameAndUser(String name, User user);

    @EntityGraph(attributePaths = {"applications"})
    Page<Company> findAllByUserId(Long userId, Pageable pageable);


}
