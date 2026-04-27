package com.jobtracker.application.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtracker.application.JobApplicationTrackerApplication;
import com.jobtracker.application.exception.InvalidDataException;
import com.jobtracker.application.model.entity.Application;
import com.jobtracker.application.model.entity.Company;
import com.jobtracker.application.model.entity.User;
import com.jobtracker.application.model.enums.Status;
import com.jobtracker.application.model.request.CompanyRequest;
import com.jobtracker.application.repositories.ApplicationRepository;
import com.jobtracker.application.repositories.CompanyRepository;
import com.jobtracker.application.repositories.UserRepository;
import com.jobtracker.application.security.JwtUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = JobApplicationTrackerApplication.class)
@AutoConfigureMockMvc
public class CompanyControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    private String currentJwt;
    private Long companyId;
    private Long applicationId;

    @BeforeEach
    void setUpData() {
        Company company = new Company();
        company.setName("Test Company");
        company.setAddress("Test Address");
        company.setUser(userRepository.findById(1L)
                .orElseThrow(() -> new InvalidDataException("User not found")));
        companyId = companyRepository.save(company).getId();

        Application application = new Application();
        application.setPosition("Test Application");
        application.setCompany(company);
        application.setStatus(Status.IN_PROGRESS);
        applicationId = applicationRepository.save(application).getId();


    }


    @AfterEach
    void cleanUp() {
        applicationRepository.deleteById(applicationId);
        companyRepository.deleteById(companyId);
    }

    @BeforeAll
    void authorize() {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new InvalidDataException("User not found"));


        this.currentJwt = "Bearer " + jwtUtils.generateJwtToken(user.getUsername());

    }


    @Test
    @Transactional
    void createCompany_201_CREATED() throws Exception {
        CompanyRequest request = new CompanyRequest("test", null, null, null, null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/companies")
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"));

        assertTrue(companyRepository.findByName("test").isPresent());
    }

    @Test
    @Transactional
    void createCompany_403_noAccess() throws Exception {
        CompanyRequest request = new CompanyRequest("test", null, null, null, null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/companies")
                        .header(HttpHeaders.AUTHORIZATION, "falseJwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    void createCompany_409_CompanyAlreadyExists() throws Exception {
        CompanyRequest request = new CompanyRequest("Test Company", null, null, null, null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/companies")
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());

        assertTrue(companyRepository.findByName("Test Company").isPresent());
    }

    @Test
    @Transactional
    void createCompany_400_InvalidData() throws Exception {
        CompanyRequest request = new CompanyRequest(null, null, null, null, null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/companies")
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }


    @Test
    @Transactional
    void getAllCompanies_200_OK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/companies")
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").exists());

    }

    @Test
    @Transactional
    void getAllCompanies_403_noAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/companies")
                        .header(HttpHeaders.AUTHORIZATION, "falseJwt")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    void getCompanyById_200_OK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/companies/{id}", companyId)
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(companyId));
    }

    @Test
    @Transactional
    void getCompanyById_403_noAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/companies/{id}", companyId)
                        .header(HttpHeaders.AUTHORIZATION, "falseJwt")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    @Transactional
    void updateCompany_200_OK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/companies/{id}", companyId)
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CompanyRequest("test", null, null, null, null)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(companyId));

    }

    @Test
    @Transactional
    void updateCompany_403_noAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/companies/{id}", companyId)
                        .header(HttpHeaders.AUTHORIZATION, "falseJwt")
                        .content(objectMapper.writeValueAsBytes(new CompanyRequest("test", null, null, null, null)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    @Transactional
    void updateCompany_400_InvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/companies/{id}", companyId)
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new CompanyRequest(null, null, null, null, null)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    void deleteCompanyById_200_OK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/companies/{id}", companyId)
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertFalse(companyRepository.findById(companyId).isPresent());
    }

    @Test
    @Transactional
    void deleteCompanyById_403_noAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/companies/{id}", companyId)
                        .header(HttpHeaders.AUTHORIZATION, "falseJwt")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }
}
