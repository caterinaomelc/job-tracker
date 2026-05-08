package com.jobtracker.application.repositories;

import com.jobtracker.application.model.entity.Application;
import com.jobtracker.application.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ApplicationRepository extends JpaRepository<Application,Long> {

    Page<Application> findAllByCompanyUserId(Long userId, Pageable pageable);

    Page<Application> findAllByCompanyUserIdAndStatus(Long userId, Pageable pageable, Status status);
}
