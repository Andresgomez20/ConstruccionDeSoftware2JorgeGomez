package app.domain.services;

import org.springframework.stereotype.Service;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.NaturalPersonClient;
import app.domain.ports.NaturalPersonClientPort;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterNaturalPerson {

    private final NaturalPersonClientPort naturalPersonClientPort;
    private final LogOperation logOperation; // 1. Inyectamos la bitácora

    public RegisterNaturalPerson(NaturalPersonClientPort naturalPersonClientPort, LogOperation logOperation) {
        this.naturalPersonClientPort = naturalPersonClientPort;
        this.logOperation = logOperation;
    }

    public void register(NaturalPersonClient client) {
        if (naturalPersonClientPort.existsByIdentificationNumber(client.getIdentificationNumber())) {
            throw new BusinessException("Ya existe un cliente persona natural con este número de identificación.");
        }
        
        // Guardar en MySQL
        naturalPersonClientPort.save(client);

        // REGISTRO EN BITÁCORA
        Map<String, Object> details = new HashMap<>();
        details.put("fullName", client.getFullName());
        details.put("idNumber", client.getIdentificationNumber());
        details.put("email", client.getEmail());
        details.put("address", client.getAddress());
        details.put("clientType", "NATURAL");

        logOperation.record(
            0L, 
            "ADMIN/REGISTRATION", 
            "NATURAL_PERSON_REGISTERED", 
            client.getIdentificationNumber(), 
            details
        );
    }
}