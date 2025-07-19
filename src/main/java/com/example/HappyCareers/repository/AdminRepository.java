package com.example.HappyCareers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HappyCareers.entity.Admin;
import com.example.HappyCareers.entity.Job;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
    
    

}

