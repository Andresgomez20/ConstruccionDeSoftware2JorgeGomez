package app.domain.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CompanyClient {

    private String taxIdentificationNumber; // NIT
    private String businessName;            // Razón Social
    private String email;
    private String phone;
    private String address;
    private String legalRepresentativeId;   // Referencia al identificationNumber de un NaturalPersonClient
    
}