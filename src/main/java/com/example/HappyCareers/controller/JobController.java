package com.example.HappyCareers.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.HappyCareers.entity.Job;
import com.example.HappyCareers.repository.JobRepository;

@Controller 
public class JobController {

    @Autowired
    private JobRepository jobRepo;

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(required = false) String location,
                       Model model) {
    	 if (keyword != null && keyword.trim().isEmpty()) {
    	        keyword = null;
    	    }
    	 if (location != null && location.trim().isEmpty()) {
    	        location = null;
    	    }
        Pageable pageable = PageRequest.of(page, 5); // 5 jobs per page

        Page<Job> jobPage = jobRepo.searchJobs(keyword, location, pageable);

        // ✅ Use jobPage.getContent() to get only current page jobs
        model.addAttribute("jobs", jobPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", jobPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("location", location);

        return "index";
    }


    @GetMapping("/jobs")
    public String redirectToHome() {
        return "redirect:/";
    }
    
    @GetMapping("/job/{id}")
    public String showJobDetails(@PathVariable("id") Long id, Model model) {
        Job job = jobRepo.findById(id).orElse(null);
        if (job == null) {
            return "error"; // Or a custom error page
        }
        model.addAttribute("job", job);
        return "job-detail"; // You should have job-detail.html in templates
    }
    
    @GetMapping("/search")
    public String searchJobs(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) String location,
                             @RequestParam(required = false) String sortBy,
                             Model model) {

        List<Job> jobs;

        // Combine conditions
        if (sortBy != null && sortBy.equals("salaryHigh")) {
            jobs = jobRepo.findByKeywordAndLocationOrderBySalaryDesc(keyword, location);
        } else if (sortBy != null && sortBy.equals("salaryLow")) {
            jobs = jobRepo.findByKeywordAndLocationOrderBySalaryAsc(keyword, location);
        } else if (sortBy != null && sortBy.equals("newest")) {
            jobs = jobRepo.findByKeywordAndLocationOrderByPostedDateDesc(keyword, location);
        } else if (sortBy != null && sortBy.equals("oldest")) {
            jobs = jobRepo.findByKeywordAndLocationOrderByPostedDateAsc(keyword, location);
        } else {
            jobs = jobRepo.findByKeywordAndLocation(keyword, location);
        }

        model.addAttribute("jobs", jobs);
        return "index";
    }


    
    @GetMapping("/jobs/today")
    public String jobsPostedToday(Model model) {
        List<Job> jobs = jobRepo.findByPostedDate(LocalDate.now());
        model.addAttribute("jobs", jobs);
        return "index";
    }

    @GetMapping("/jobs/recent")
    public String jobsPostedLast7Days(Model model) {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        List<Job> jobs = jobRepo.findByPostedDateAfter(sevenDaysAgo);
        model.addAttribute("jobs", jobs);
        return "index";
    }

    @GetMapping("/jobs/sort/date/newest")
    public String jobsSortedByNewest(Model model) {
        List<Job> jobs = jobRepo.findAllByOrderByPostedDateDesc();
        model.addAttribute("jobs", jobs);
        return "index";
    }

    @GetMapping("/jobs/sort/date/oldest")
    public String jobsSortedByOldest(Model model) {
        List<Job> jobs = jobRepo.findAllByOrderByPostedDateAsc();
        model.addAttribute("jobs", jobs);
        return "index";
    }
    
    @GetMapping("/admin/editJob/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Job job = jobRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
        model.addAttribute("job", job);
        return "edit-job"; // View name
    }



}   




