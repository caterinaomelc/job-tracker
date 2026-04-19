package com.jobtracker.application.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtracker.application.JobApplicationTrackerApplication;
import com.jobtracker.application.exception.InvalidDataException;
import com.jobtracker.application.model.entity.Application;
import com.jobtracker.application.model.entity.Company;
import com.jobtracker.application.model.entity.User;
import com.jobtracker.application.model.enums.Status;
import com.jobtracker.application.model.request.ApplicationRequest;
import com.jobtracker.application.repositories.ApplicationRepository;
import com.jobtracker.application.repositories.CompanyRepository;
import com.jobtracker.application.repositories.UserRepository;
import com.jobtracker.application.security.JwtUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = JobApplicationTrackerApplication.class)
@AutoConfigureMockMvc
public class ApplicationControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private String currentJwt;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Long companyId;
    private Long applicationId;

    @BeforeEach
    void setUpData() {
        Company company = new Company();
        company.setName("Test Company");
        company.setAddress("Test Address");
        company.setApplications(new ArrayList<Application>());
        company.setUser(userRepository.findById(1L)
                .orElseThrow(() -> new InvalidDataException("User not found")));
        companyId = companyRepository.save(company).getId();

        Application application = new Application();
        application.setPosition("Test Application");
        application.setCompany(company);
        application.setStatus(Status.IN_PROGRESS);
        applicationId = applicationRepository.save(application).getId(); // ← добавь это

    }

    @BeforeAll
    void authorize() {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new InvalidDataException("User not found"));


        this.currentJwt = "Bearer " + jwtUtils.generateJwtToken(user.getUsername());
    }


    @Test
    @Transactional
    void addApplication_201_CREATED() throws Exception {
        ApplicationRequest request = new ApplicationRequest("test", null, null, null, null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/companies/{companyId}/applications", companyId)
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.position").value("test"));

    }

    @Test
    @Transactional
    void addApplication_404_companyDoesNotExist() throws Exception {
        ApplicationRequest request = new ApplicationRequest("test", null, null, null, null);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/companies/{companyId}/applications", 999L)
                .header(HttpHeaders.AUTHORIZATION, currentJwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    void addApplication_403_noAccess() throws Exception {
        ApplicationRequest request = new ApplicationRequest("test", null, null, null, null);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/companies/{companyId}/applications", companyId)
                        .header(HttpHeaders.AUTHORIZATION, "falseJwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    void addApplication_400_invalidData() throws Exception {
        ApplicationRequest request = new ApplicationRequest(null, null, null, null, null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/companies/{companyId}/applications", companyId)
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }





    @Test
    @Transactional
    void updateApplication_200_OK() throws Exception {
        ApplicationRequest request = new ApplicationRequest("test", null, null, null, null);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/companies/{companyId}/applications/{id}", companyId, applicationId)
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.position").value("test"));
    }

    @Test
    @Transactional
    void updateApplication_403_noAccess() throws Exception {
        ApplicationRequest request = new ApplicationRequest("test", null, null, null, null);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/companies/{companyId}/applications/{id}", companyId, applicationId)
                        .header(HttpHeaders.AUTHORIZATION, "falseJwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    void updateApplication_400_invalidData() throws Exception {
        ApplicationRequest request = new ApplicationRequest(null, null, null, null, null);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/companies/{companyId}/applications/{id}", companyId, applicationId)
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());}

    @Test
    @Transactional
    void deleteApplication_204_noContent() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/companies/{companyId}/applications/{id}", companyId, applicationId)
                .header(HttpHeaders.AUTHORIZATION, currentJwt)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertFalse(applicationRepository.findById(applicationId).isPresent());
    }

    @Test
    @Transactional
    void deleteApplication_403_noAccess() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/companies/{companyId}/applications/{id}", companyId, applicationId)
                        .header(HttpHeaders.AUTHORIZATION, "falseJwt")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }


    @Test
    @Transactional
    void getApplication_200_OK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/applications")
                .header(HttpHeaders.AUTHORIZATION, currentJwt)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    @Transactional
    void getApplication_403_noAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/applications")
                        .header(HttpHeaders.AUTHORIZATION, "falseJwt")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


}
