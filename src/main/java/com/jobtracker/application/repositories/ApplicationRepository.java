package com.jobtracker.application.repositories;

import com.jobtracker.application.model.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ApplicationRepository extends JpaRepository<Application,Long> {

    Page<Application> findAllByCompanyUserId(Long userId, Pageable pageable);
}
