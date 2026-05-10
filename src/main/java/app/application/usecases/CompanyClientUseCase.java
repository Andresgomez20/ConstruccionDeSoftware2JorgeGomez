package app.application.usecases;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.CompanyClient;
import app.domain.services.RegisterCompany; // <-- Usamos tu nombre real
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyClientUseCase {

    @Autowired
    private RegisterCompany registerCompany;

    public CompanyClientUseCase(RegisterCompany registerCompany) {
        this.registerCompany = registerCompany;
    }

    public void registerCompanyClient(CompanyClient client) throws BusinessException {
        registerCompany.execute(client); // <-- Llamamos a execute
    }
    
    // (Si tienes un servicio para buscar, lo agregas aquí después)
}