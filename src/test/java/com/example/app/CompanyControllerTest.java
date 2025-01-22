package com.example.app;

import com.example.app.controller.CompanyController;
import com.example.app.model.Company;
import com.example.app.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    private Company company;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
        company = new Company("123456", "Company Name", "Company Address", "123-456-789");
    }

    @Test
    void testGetAllCompanies() throws Exception {
        when(companyService.getAllCompanies()).thenReturn(List.of(company));
        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Company Name"))
                .andExpect(jsonPath("$[0].address").value("Company Address"));
    }

    @Test
    void testGetCompanyById() throws Exception {
        when(companyService.getCompanyById("123456")).thenReturn(Optional.of(company));
        mockMvc.perform(get("/api/companies/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Company Name"))
                .andExpect(jsonPath("$.address").value("Company Address"));
    }

    @Test
    void testCreateCompany() throws Exception {
        when(companyService.createCompany(any(Company.class))).thenReturn(company);
        mockMvc.perform(post("/api/companies")
                .contentType("application/json")
                .content("{ \"nit\": \"123456\", \"name\": \"Company Name\", \"address\": \"Company Address\", \"phone\": \"123-456-789\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Company Name"))
                .andExpect(jsonPath("$.address").value("Company Address"));
    }

    @Test
    void testUpdateCompany() throws Exception {
        when(companyService.updateCompany(eq("123456"), any(Company.class))).thenReturn(company);
        mockMvc.perform(put("/api/companies/123456")
                .contentType("application/json")
                .content("{ \"nit\": \"123456\", \"name\": \"Company Name\", \"address\": \"Company Address\", \"phone\": \"123-456-789\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Company Name"))
                .andExpect(jsonPath("$.address").value("Company Address"));
    }

    @Test
    void testDeleteCompany() throws Exception {
        mockMvc.perform(delete("/api/companies/123456"))
                .andExpect(status().isNoContent());
        verify(companyService, times(1)).deleteCompany("123456");
    }
}
