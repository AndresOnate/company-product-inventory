package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.model.Company;
import com.example.app.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling business logic related to the Company entity.
 * It interacts with the CompanyRepository to perform CRUD operations.
 * Provides methods to get, create, update, and delete companies.
 * The @Service annotation indicates that this class is a service component in the Spring application context.
 */
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(String id) {
        return companyRepository.findById(id);
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(String id, Company companyDetails) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            company.setName(companyDetails.getName());
            company.setAddress(companyDetails.getAddress());
            company.setPhone(companyDetails.getPhone());
            return companyRepository.save(company);
        }
        return null;
    }

    public void deleteCompany(String id) {
        companyRepository.deleteById(id);
    }
}
