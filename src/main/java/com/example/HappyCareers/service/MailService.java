package com.example.HappyCareers.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.HappyCareers.entity.Application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    //IT IS FOR SIMPLE TEXT MAIL
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nithikaa24@gmail.com"); // same as spring.mail.username
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    
    //FOR STYLED HTML MAIL
    public void sendStyledEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setFrom("nithikaa24@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // `true` means HTML content
        
        mailSender.send(message);
    }
    
    public void sendApplicationEmailWithAttachment(Application application, File resumeFile) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);  // true = multipart

        helper.setTo("nithikaajagadesh@gmail.com");
        helper.setSubject("New Application: " + application.getName());

        String htmlContent = "<html><body>" +
                "<h2>New Job Application</h2>" +
                "<p><strong>Name:</strong> " + application.getName() + "</p>" +
                "<p><strong>Email:</strong> " + application.getEmail() + "</p>" +
                "<p><strong>Job ID:</strong> " + application.getJobId() + "</p>" +
                "<p>Resume is attached to this email.</p>" +
                "</body></html>";

        helper.setText(htmlContent, true);

        // ✅ Attach the resume file
        FileSystemResource file = new FileSystemResource(resumeFile);
        helper.addAttachment(resumeFile.getName(), file);

        mailSender.send(message);
    }

}

