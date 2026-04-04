package app.domain.ports;

import java.util.List;

import app.domain.models.entities.NaturalPersonClient;

public interface NaturalPersonClientPort {

    boolean existsByIdentificationNumber(String identificationNumber);
    void save(NaturalPersonClient client);
    NaturalPersonClient findByIdentificationNumber(String identificationNumber);
    List<NaturalPersonClient> findAll();

}