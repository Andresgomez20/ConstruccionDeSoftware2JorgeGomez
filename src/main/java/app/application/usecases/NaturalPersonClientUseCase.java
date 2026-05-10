package app.application.usecases;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.NaturalPersonClient;
import app.domain.services.RegisterNaturalPerson; // <-- Usamos tu nombre real
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NaturalPersonClientUseCase {

    @Autowired
    private RegisterNaturalPerson registerClient;

    public NaturalPersonClientUseCase(RegisterNaturalPerson registerClient) {
        this.registerClient = registerClient;
    }

    public void registerNaturalPersonClient(NaturalPersonClient client) throws BusinessException {
        registerClient.execute(client); // <-- Llamamos a execute
    }
    
    // (Si tienes un servicio para buscar, lo agregas aquí después)
}