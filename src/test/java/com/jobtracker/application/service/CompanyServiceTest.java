package com.jobtracker.application.service;

import com.jobtracker.application.exception.InvalidDataException;
import com.jobtracker.application.exception.NotFoundException;
import com.jobtracker.application.mapper.CompanyMapper;
import com.jobtracker.application.model.entity.Company;
import com.jobtracker.application.model.entity.User;
import com.jobtracker.application.model.request.CompanyRequest;
import com.jobtracker.application.model.response.CompanyResponse;
import com.jobtracker.application.repositories.CompanyRepository;
import com.jobtracker.application.repositories.UserRepository;
import com.jobtracker.application.service.impl.CompanyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Mock
    CompanyRepository companyRepository;

    @Mock
    CompanyMapper companyMapper;

    @Mock
    UserRepository userRepository;


    @InjectMocks
    CompanyServiceImpl companyService;

    private Company testCompany;
    private User testUser;
    private List<Company> testCompanies = new ArrayList<>();
    private CompanyRequest testCompanyRequest;
    private CompanyResponse testCompanyResponse;

    @BeforeEach
    void setUp() {

        testUser =  new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(testUser.getUsername(), null);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);


        testCompany = new Company();
        testCompany.setId(1L);
        testCompany.setUser(testUser);
        testCompanies.add(testCompany);

        testCompanyRequest = new CompanyRequest("test name", null, null, null, null);
        testCompanyResponse = new CompanyResponse(1L, "test name", null, null, null, null, null);

    }


    @Test
    void getAllCompanies_ReturnListOfCompanies() {
        Pageable pageable = Pageable.unpaged();
        when(companyRepository.findAllByUserId(1L, pageable))
                .thenReturn(new PageImpl<>(testCompanies));
        when(userRepository.findByUsername("testUser"))
                .thenReturn(Optional.of(testUser));

        var result = companyService.getAllCompanies(pageable);
        assertEquals(testCompanies.size(), result.getContent().size());

        verify(companyRepository, times(1)).findAllByUserId(testUser.getId(), pageable);
    }

    @Test
    void getCompanyById_ReturnsCompanyResponse() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        var result = companyService.getCompanyById(1L);

        assertEquals(companyMapper.toResponse(testCompany), result);

        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void getCompanyById_ThrowsException_WhenCompanyDoesNotExist() {
        when(companyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,  () -> companyService.getCompanyById(2L));

        verify(companyRepository, times(1)).findById(2L);
    }


    @Test
    void deleteCompany(){
        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        companyService.deleteCompany(1L);

        verify(companyRepository, times(1)).delete(testCompany);

    }

    @Test
    void deleteCompany_ThrowException_CompanyDoesNotExist(){

        when(companyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> companyService.deleteCompany(2L));
    }

    @Test
    void addCompany(){
        when(companyMapper.toEntity(testCompanyRequest)).thenReturn(testCompany);
        when(companyRepository.save(testCompany)).thenReturn(testCompany);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(companyMapper.toResponse(testCompany)).thenReturn(testCompanyResponse);

        var result = companyService.addCompany(testCompanyRequest);
        assertEquals(testCompanyResponse, result);

        verify(companyRepository, times(1)).save(testCompany);
        verify(companyMapper, times(1)).toEntity(testCompanyRequest);
        verify(companyMapper, times(1)).toResponse(testCompany);
        verify(userRepository, times(1)).findByUsername("testUser");
    }

}
