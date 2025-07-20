package com.example.HappyCareers.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.HappyCareers.entity.Application;
import com.example.HappyCareers.repository.ApplicationRepository;
import com.example.HappyCareers.repository.JobRepository;
import com.example.HappyCareers.service.HCaptchaService;
import com.example.HappyCareers.service.MailService;



@Controller
public class ApplicationController {
 
    @Autowired
    private ApplicationRepository applicationRepo;
    
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private JobRepository jobRepo;
    
    @Autowired
    private HCaptchaService hCaptchaService;
    
    @Autowired
    private MailService mailService;

    @GetMapping("/job/{id}/apply")
    public String showApplicationForm(@PathVariable("id") Long jobId, Model model) {
        model.addAttribute("application", new Application());
        model.addAttribute("jobId", jobId);
        return "application-form";
    } 
 

	/*	@PostMapping("/job/{id}/apply")
		public String submitApplication(
		        @PathVariable("id") Long jobId,
		        @ModelAttribute Application application,
		        @RequestParam("h-captcha-response") String captchaResponse,
		        @RequestParam("resumeFile") MultipartFile resumeFile,
		        Model model
		) {
		    boolean isVerified = hCaptchaService.verifyCaptcha(captchaResponse);
		
		    if (!isVerified) {
		        model.addAttribute("error", "Captcha verification failed. Please try again.");
		        model.addAttribute("application", application);
		        model.addAttribute("jobId", jobId);
		        return "application-form";
		    }
	
		    application.setId(null);
		    application.setJobId(jobId);
		    applicationRepo.save(application);
		
		 // ✅ Send simple email to applicant
		    String subject = "Application Received for Job ID: " + jobId;
		    String body = "Hello " + application.getName() + ",\n\nYour application has been received. Thank you!";
		    mailService.sendEmail(application.getEmail(), subject, body);

		    // ✅ Send styled HTML email to admin
		    try {
		    	String uploadDir = "uploads/";
		        String originalFileName = resumeFile.getOriginalFilename();
		        String filePath = uploadDir + System.currentTimeMillis() + "_" + originalFileName;

		        File dest = new File(filePath);
		        dest.getParentFile().mkdirs(); // create folder if not exists
		        resumeFile.transferTo(dest);

		        // Save file path in DB instead of link
		        application.setResumeUrl(filePath);
		        application.setJobId(jobId);
		        application.setCreatedAt(LocalDateTime.now());
		        String htmlContent = "<html>" +
		            "<body style='font-family: Arial, sans-serif;'>" +
		            "<h2 style='color: #2c3e50;'>New Job Application Received</h2>" +
		            "<p><strong>Name:</strong> " + application.getName() + "</p>" +
		            "<p><strong>Email:</strong> " + application.getEmail() + "</p>" +
	
		            "<p><strong>Job Applied For (ID):</strong> " + jobId + "</p>" +
		            "<p><strong>Resume Link:</strong> <a href='" + application.getResumeUrl() + "'>View Resume</a></p>" +
		            "<hr style='margin-top: 20px;'/>" +
		            "<p style='font-size: 12px; color: #888;'>This is an automated message from Happy Careers.</p>" +
		            "</body></html>";

		        mailService.sendStyledEmail(
		            "admin@example.com", // ✅ Replace with real admin email
		            "New Application from " + application.getName(),
		            htmlContent
		        );
		    } catch (Exception e) {
		        System.out.println("Failed to send styled email: " + e.getMessage());
		        // You may log it or handle accordingly
		    }
		    return "redirect:/application-success";
		}*/
		
    @PostMapping("/job/{id}/apply")
    public String submitApplication(
            @PathVariable("id") Long jobId,
            @ModelAttribute Application application,
            @RequestParam("h-captcha-response") String captchaResponse,
            @RequestParam("resumeFile") MultipartFile resumeFile,
            Model model
    ) {
        boolean isVerified = hCaptchaService.verifyCaptcha(captchaResponse);

        if (!isVerified) {
            model.addAttribute("error", "Captcha verification failed. Please try again.");
            model.addAttribute("application", application);
            model.addAttribute("jobId", jobId);
            return "application-form";
        }

        try {
            // ✅ Save resume to local folder
            String uploadDir = System.getProperty("user.home") + "/happycareers-resumes/";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) uploadPath.mkdirs();  // Create dir if it doesn't exist

            String originalFileName = resumeFile.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalFileName;
            File savedFile = new File(uploadDir + fileName);
            resumeFile.transferTo(savedFile);  // Save uploaded file

            // ✅ Save application to DB
            application.setId(null);
            application.setJobId(jobId);
            application.setResumeUrl(savedFile.getAbsolutePath());  // Store file path
            application.setCreatedAt(LocalDateTime.now());
            application.setResumeFileName(fileName); 

            applicationRepo.save(application);

            // ✅ Send email to applicant
            mailService.sendEmail(
                application.getEmail(),
                "Application Received - Happy Careers",
                "Hi " + application.getName() + ",\n\nThank you for applying. We have received your application for Job ID: " + jobId + "."
            );

            // ✅ Send email to admin with resume attached
            String htmlContent = "<html><body>" +
                    "<h3>New Job Application Received</h3>" +
                    "<p><strong>Name:</strong> " + application.getName() + "</p>" +
                    "<p><strong>Email:</strong> " + application.getEmail() + "</p>" +
                    "<p><strong>Job ID:</strong> " + jobId + "</p>" +
                    "<p><strong>Resume:</strong> Attached</p>" +
                    "<br><hr><p style='color:gray;'>This is an automated message from Happy Careers.</p>" +
                    "</body></html>";

            mailService.sendApplicationEmailWithAttachment(application, savedFile);  // ✅ sends the actual file

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Something went wrong during file upload or email sending.");
            model.addAttribute("application", application);
            model.addAttribute("jobId", jobId);
            return "application-form";
        }

        return "redirect:/application-success";
    }


		    	


    
    @GetMapping("/application-success")
    public String showSuccessPage() {
        return "application-success"; // name of the HTML file
    }
    
    @GetMapping("/applications")
    public String viewAllApplications(Model model) {
        List<Application> applications = applicationRepo.findAll();
        model.addAttribute("applications", applications);
        return "application-list"; // Create this HTML page
    }


}

