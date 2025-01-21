package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.app.model.Company;
import com.example.app.service.CompanyService;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for handling REST endpoints related to the Company entity.
 * It exposes endpoints for getting, creating, updating, and deleting companies.
 */
@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{nit}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String nit) {
        Optional<Company> companyOptional = companyService.getCompanyById(nit);
        return companyOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        Company createdCompany = companyService.createCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @PutMapping("/{nit}")
    public ResponseEntity<Company> updateCompany(@PathVariable String nit, @RequestBody Company companyDetails) {
        Company updatedCompany = companyService.updateCompany(nit, companyDetails);
        return updatedCompany != null ? ResponseEntity.ok(updatedCompany)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{nit}")
    public ResponseEntity<Void> deleteCompany(@PathVariable String nit) {
        companyService.deleteCompany(nit);
        return ResponseEntity.noContent().build();
    }
}
