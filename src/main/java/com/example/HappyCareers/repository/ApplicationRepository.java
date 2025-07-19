package com.example.HappyCareers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.HappyCareers.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application,Long>{
 

    List<Application> findByJobId(Long jobId);
    
	@Query("SELECT a FROM Application a ORDER BY a.createdAt DESC")
    List<Application> findAllApplicationsOrderByDate();
}
