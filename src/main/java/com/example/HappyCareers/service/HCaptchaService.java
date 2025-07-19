package com.example.HappyCareers.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;



@Service
public class HCaptchaService {

    @Value("${hcaptcha.secret}")
    private String secret;

    public boolean verifyCaptcha(String response) {
        RestTemplate restTemplate = new RestTemplate();
        String verifyUrl = "https://hcaptcha.com/siteverify";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", secret);
        params.add("response", response);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(verifyUrl, request, Map.class);

        Map<String, Object> body = responseEntity.getBody();
        return body != null && Boolean.TRUE.equals(body.get("success"));
    }
}
