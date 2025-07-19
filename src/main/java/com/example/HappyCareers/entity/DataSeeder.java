package com.example.HappyCareers.entity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.HappyCareers.repository.JobRepository;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final JobRepository jobRepository;

    public DataSeeder(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
 
    @Override
    public void run(String... args) throws Exception {
        if (jobRepository.count() == 0) {
            List<Job> jobs = List.of(
                new Job("Java Developer", "Develop enterprise Java apps using Spring Boot", "Chennai", "Infosys", LocalDate.of(2025,7,11), 800000.0),
                new Job("Frontend Developer", "Build UI with React and TailwindCSS", "Bangalore", "Wipro", LocalDate.of(2025,7,10), 750000.0),
                new Job("Data Analyst", "Analyze business data using Power BI and SQL", "Hyderabad", "Deloitte", LocalDate.of(2025,7,9), 700000.0),
                new Job("DevOps Engineer", "Maintain CI/CD pipelines and infrastructure", "Remote", "TCS", LocalDate.of(2025,7,8), 850000.0),
                new Job("Cybersecurity Intern", "Assist with security audits and network defense", "Chennai", "Secure Audit India", LocalDate.of(2025,07,07), 300000.0)
            );
            jobRepository.saveAll(jobs);
        }
    }
}
