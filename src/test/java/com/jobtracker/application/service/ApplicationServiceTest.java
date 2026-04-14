package com.jobtracker.application.service;

import com.jobtracker.application.exception.InvalidDataException;
import com.jobtracker.application.mapper.ApplicationMapper;
import com.jobtracker.application.model.entity.Application;
import com.jobtracker.application.model.entity.Company;
import com.jobtracker.application.model.entity.User;
import com.jobtracker.application.model.request.ApplicationRequest;
import com.jobtracker.application.model.response.ApplicationResponse;
import com.jobtracker.application.repositories.ApplicationRepository;
import com.jobtracker.application.repositories.CompanyRepository;
import com.jobtracker.application.repositories.UserRepository;
import com.jobtracker.application.service.impl.ApplicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;



@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    CompanyRepository companyRepository;

    @Mock
    ApplicationRepository applicationRepository;

    @Mock
    ApplicationMapper applicationMapper;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ApplicationServiceImpl applicationService;

    private Company testCompany;
    private User testUser;
    private Application testApplication;
    private ApplicationRequest testApplicationRequest;
    private ApplicationResponse testApplicationResponse;
    private List<Company> testCompanies = new ArrayList<>();

    @BeforeEach
    void setUp() {

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testCompany = new Company();
        testCompany.setId(1L);
        testCompany.setUser(testUser);


        Authentication authentication =
                new UsernamePasswordAuthenticationToken(testUser.getUsername(), null);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);


        testApplication = new Application();
        testApplication.setId(1L);
        testApplication.setCompany(testCompany);
        testApplicationRequest = new ApplicationRequest("test position", null, null, null, null);
        testApplicationResponse = new ApplicationResponse("test position", null, null, null, null, null);

        testCompanies.add(testCompany);
        testUser.setCompanies(testCompanies);

    }

    @Test
    void addApplication_CompanyExists() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));
        when(applicationMapper.toEntity(testApplicationRequest)).thenReturn(testApplication);
        when(applicationRepository.save(testApplication)).thenReturn(testApplication);
        when(applicationMapper.toResponse(testApplication)).thenReturn(testApplicationResponse);

        assertEquals(applicationService.addApplication(testApplicationRequest, 1L), testApplicationResponse);

        verify(companyRepository, times(1)).findById(1L);
        verify(applicationMapper, times(1)).toEntity(testApplicationRequest);
        verify(applicationMapper, times(1)).toResponse(testApplication);
        verify(applicationRepository, times(1)).save(testApplication);
    }


    @Test
    void addApplication_ThrowsException_CompanyDoesNotExist() {
        when(companyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(InvalidDataException.class, () -> applicationService.addApplication(testApplicationRequest, 2L));

        verify(companyRepository, times(1)).findById(2L);
    }


    @Test
    void updateApplication() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(testApplication));
        when(applicationMapper.updateApplication(testApplicationRequest, testApplication)).thenReturn(testApplication);
        when(applicationRepository.save(testApplication)).thenReturn(testApplication);
        when(applicationMapper.toResponse(testApplication)).thenReturn(testApplicationResponse);

        assertEquals(applicationService.updateApplication(testApplicationRequest, 1L, 1L), testApplicationResponse);

        verify(companyRepository, times(1)).findById(1L);
        verify(applicationRepository, times(1)).findById(1L);
        verify(applicationMapper, times(1)).updateApplication(testApplicationRequest, testApplication);
        verify(applicationRepository, times(1)).save(testApplication);
        verify(applicationMapper, times(1)).toResponse(testApplication);
    }

    @Test
    void updateApplication_ThrowsException_CompanyDoesNotExist() {
        when(companyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(InvalidDataException.class, () -> applicationService.updateApplication(testApplicationRequest, 2L, 1L));

        verify(companyRepository, times(1)).findById(2L);
    }

    @Test
    void updateApplication_ThrowsException_ApplicationDoesNotExist(){
        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));
        when(applicationRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(InvalidDataException.class, () -> applicationService.updateApplication(testApplicationRequest, 1L, 2L));

        verify(companyRepository, times(1)).findById(1L);
        verify(applicationRepository, times(1)).findById(2L);
    }

    @Test
    void updateApplication_ThrowsException_CompanyAndApplicationDoNotMatch(){
        Company anotherCompany = new Company();
        anotherCompany.setId(2L);
        when(companyRepository.findById(2L)).thenReturn(Optional.of(anotherCompany));
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(testApplication));

        assertThrows(InvalidDataException.class, () -> applicationService.updateApplication(testApplicationRequest, 2L, 1L));

        verify(companyRepository, times(1)).findById(2L);
        verify(applicationRepository, times(1)).findById(1L);
    }

    @Test
    void deleteApplication(){
        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(testApplication));

        applicationService.deleteApplication(1L, 1L);

        verify(companyRepository, times(1)).findById(1L);
        verify(applicationRepository, times(1)).findById(1L);
        verify(applicationRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteApplication_ThrowsException_CompanyDoesNotExist() {
        when(companyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(InvalidDataException.class, () -> applicationService.deleteApplication(2L, 1L));

        verify(companyRepository, times(1)).findById(2L);
    }

    @Test
    void deleteApplication_ThrowsException_ApplicationDoesNotExist(){
        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));
        when(applicationRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(InvalidDataException.class, () -> applicationService.deleteApplication(1L, 2L));

        verify(companyRepository, times(1)).findById(1L);
        verify(applicationRepository, times(1)).findById(2L);
    }

    @Test
    void getAllApplications() {
        testCompany.setApplications(List.of(testApplication));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(applicationMapper.toResponse(testApplication)).thenReturn(testApplicationResponse);

        var result = applicationService.getAllApplications();

        assertEquals(List.of(testApplicationResponse), result);

        verify(applicationMapper, times(1)).toResponse(testApplication);
    }

}
