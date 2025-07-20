package com.example.HappyCareers.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    @Column(name = "resume_url")
    private String resumeUrl; 
    private Long jobId;
    @Column(name = "resume_file_name")
    private String resumeFileName;

    
    @Column(nullable = false, updatable = false,name = "created_at")
    private LocalDateTime createdAt;

    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}



	public String getResumeFileName() {
		return resumeFileName;
	}



	public void setResumeFileName(String resumeFileName) {
		this.resumeFileName = resumeFileName;
	}



	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getResumeUrl() {
		return resumeUrl;
	}
	public void setResumeUrl(String resumeUrl) {
		this.resumeUrl = resumeUrl;
	}
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
    
    
}
