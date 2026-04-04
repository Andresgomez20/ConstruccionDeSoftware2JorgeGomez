package app.domain.ports;

import java.util.List;

import app.domain.models.entities.CompanyClient;

public interface CompanyClientPort {

    boolean existsByTaxIdentificationNumber(String taxIdentificationNumber);
    void save(CompanyClient company);
    CompanyClient findByTaxIdentificationNumber(String taxIdentificationNumber);
    List<CompanyClient> findAll();

}