package app.domain.services;

import org.springframework.stereotype.Service;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.CompanyClient;
import app.domain.ports.CompanyClientPort;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterCompany {

    private final CompanyClientPort companyClientPort;
    private final LogOperation logOperation;

    public RegisterCompany(CompanyClientPort companyClientPort, LogOperation logOperation) {
        this.companyClientPort = companyClientPort;
        this.logOperation = logOperation;
    }

    public void register(CompanyClient company) {
        // Validación de unicidad por NIT
        if (companyClientPort.existsByTaxIdentificationNumber(company.getTaxIdentificationNumber())) {
            throw new BusinessException("Ya existe una empresa con este número de identificación tributaria (NIT).");
        }
        
        // Guardar en MySQL
        companyClientPort.save(company);

        //REGISTRO EN BITÁCORA (MongoDB)
        Map<String, Object> details = new HashMap<>();
        details.put("businessName", company.getBusinessName());
        details.put("nit", company.getTaxIdentificationNumber());
        details.put("email", company.getEmail());
        details.put("legalRepresentative", company.getLegalRepresentativeId());
        details.put("clientType", "COMPANY");

        logOperation.record(
            0L, 
            "ADMIN/REGISTRATION", 
            "COMPANY_CLIENT_REGISTERED", 
            company.getTaxIdentificationNumber(), 
            details
        );
    }
}