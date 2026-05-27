package app.application.usecases;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.CompanyClient;
import app.domain.services.RegisterCompany;
import app.domain.ports.CompanyClientPort; 
import org.springframework.stereotype.Service;

@Service
public class CompanyClientUseCase {

    private final RegisterCompany registerCompany;
    private final CompanyClientPort companyClientPort;

    public CompanyClientUseCase(RegisterCompany registerCompany, CompanyClientPort companyClientPort) {
        this.registerCompany = registerCompany;
        this.companyClientPort = companyClientPort;
    }

     //Registra un cliente empresa
    public void registerCompanyClient(CompanyClient client) throws BusinessException {
        registerCompany.execute(client); 
    }

    //Busca una empresa por su NIT 
    public CompanyClient findByNit(String nit) {
        return companyClientPort.findByTaxIdentificationNumber(nit);
    }
}