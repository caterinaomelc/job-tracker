package com.jobtracker.application.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtracker.application.JobApplicationTrackerApplication;
import com.jobtracker.application.exception.InvalidDataException;
import com.jobtracker.application.model.entity.User;
import com.jobtracker.application.model.request.CompanyRequest;
import com.jobtracker.application.repositories.CompanyRepository;
import com.jobtracker.application.repositories.UserRepository;
import com.jobtracker.application.security.JwtUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
    private JwtUtils jwtUtils;

    @Autowired
    private MockMvc mockMvc;

    private String currentJwt;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeAll
    void authorize(){
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new InvalidDataException("User not found"));


        this.currentJwt = "Bearer " + jwtUtils.generateJwtToken(user.getUsername());

    }

    @Test
    @Transactional
    void createCompany_201_CREATED() throws Exception{
        CompanyRequest request = new CompanyRequest("test", null, null, null, null);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/companies")
                .header(HttpHeaders.AUTHORIZATION,currentJwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"));

        assertTrue(companyRepository.findByName("test").isPresent());
    }

    @Test
    @Transactional
    void getAllCompanies_200_OK() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get("/companies")
                .header(HttpHeaders.AUTHORIZATION,currentJwt)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    @Transactional
    void getCompanyById_200_OK() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get("/companies/{id}",1)
                .header(HttpHeaders.AUTHORIZATION,currentJwt)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @Transactional
    void updateCompany_200_OK() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .put("/companies/{id}",1)
                .header(HttpHeaders.AUTHORIZATION,currentJwt)
                        .content(objectMapper.writeValueAsBytes(new CompanyRequest("test", null, null, null, null)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));

    }

    @Test
    @Transactional
    void deleteCompanyById_200_OK() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/companies/{id}",1)
                .header(HttpHeaders.AUTHORIZATION,currentJwt)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertFalse(companyRepository.findById(1L).isPresent());
    }
}
