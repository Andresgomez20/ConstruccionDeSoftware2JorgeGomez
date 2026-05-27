package app.application.usecases;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.NaturalPersonClient;
import app.domain.services.RegisterNaturalPerson;
import app.domain.ports.NaturalPersonClientPort; 
import org.springframework.stereotype.Service;

@Service
public class NaturalPersonClientUseCase {

    private final RegisterNaturalPerson registerClient;
    private final NaturalPersonClientPort naturalPersonClientPort;

    public NaturalPersonClientUseCase(RegisterNaturalPerson registerClient, NaturalPersonClientPort naturalPersonClientPort) {
        this.registerClient = registerClient;
        this.naturalPersonClientPort = naturalPersonClientPort;
    }

    /**
     * Registra un cliente persona natural delegando al servicio del dominio
     */
    public void registerNaturalPersonClient(NaturalPersonClient client) throws BusinessException {
        registerClient.execute(client); 
    }

    /**
     * Busca un cliente persona natural de forma directa desde el puerto
     */
    public NaturalPersonClient findByIdentificationNumber(String identificationNumber) {
        return naturalPersonClientPort.findByIdentificationNumber(identificationNumber);
    }
}