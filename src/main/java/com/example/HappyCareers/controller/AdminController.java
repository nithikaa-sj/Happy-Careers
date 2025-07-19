package com.example.HappyCareers.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.HappyCareers.entity.Application;
import com.example.HappyCareers.entity.Job;
import com.example.HappyCareers.repository.ApplicationRepository;
import com.example.HappyCareers.repository.JobRepository;

@Controller 
@RequestMapping("/admin")
public class AdminController {

	private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public AdminController(JobRepository jobRepository, ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

    @GetMapping("/postJob")
    public String showPostJobForm() {
        return "addJob";
    }

    @PostMapping("/postJob")
    public String postJob(@ModelAttribute Job job, Model model) {
    	 if (job.getPostedDate() == null) {
    	        job.setPostedDate(LocalDate.now());
    	    }
        jobRepository.save(job);
        model.addAttribute("successMessage", "Job posted successfully!");
        return "addJob";
    }
    
    @GetMapping("/home")
    public String showAdminHome(Model model) {
    	 List<Job> recentJobs = jobRepository.findTop5ByOrderByPostedDateDesc();
    	    long totalJobs = jobRepository.count();

    	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
    	    
    	    // Map formatted dates
    	    List<String> formattedDates = recentJobs.stream()
    	        .map(job -> job.getPostedDate() != null 
    	            ? job.getPostedDate().format(formatter) 
    	            : "N/A")
    	        .toList();

    	    model.addAttribute("recentJobs", recentJobs);
    	    model.addAttribute("formattedDates", formattedDates);
    	    model.addAttribute("totalJobs", totalJobs);

    	    return "adminHome";// this will be the name of the Thymeleaf template
    }
    
    
    @GetMapping("/applications")
    public String viewApplications(Model model) {
        List<Job> jobs = jobRepository.findAll();
        Map<Job, List<Application>> jobApplications = new LinkedHashMap<>();

        for (Job job : jobs) {
            List<Application> applications = applicationRepository.findByJobId(job.getId());
            jobApplications.put(job, applications);
        }

        model.addAttribute("jobApplications", jobApplications);
        return "applications";
    }
    
    @PostMapping("/editJob/{id}")
    public String updateJob(@PathVariable Long id, @ModelAttribute Job job) {
        job.setId(id); // Reassign ID to ensure update, not insert
        job.setPostedDate(LocalDate.now()); // Or keep original if needed
        jobRepository.save(job);
        return "redirect:/admin/jobs"; // Go back to admin job list
    }
    
    @PostMapping("/deleteJob/{id}")
    public String deleteJob(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jobRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Job deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete job. It may not exist or has already been deleted.");
        }
        return "redirect:/admin/home";
    }
    
    @PostMapping("/updateJob")
    public String updateJob(@ModelAttribute("job") Job job, RedirectAttributes redirectAttributes) {
        jobRepository.save(job);
        redirectAttributes.addFlashAttribute("message", "Job updated successfully!");
        return "redirect:/admin/home";  
    }
    
    
    @GetMapping("/admin/applications")
    public String viewAllApplications(Model model) {
        List<Job> jobsWithApplications = jobRepository.findAll();
        model.addAttribute("jobs", jobsWithApplications);
        return "admin/applications";
    }






}

