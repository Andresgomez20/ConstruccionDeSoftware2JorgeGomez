package app.infrastructure.adapters.api.controllers;

import app.application.usecases.CompanyClientUseCase;
import app.domain.models.entities.CompanyClient;
import app.domain.Exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyClientController {

    private final CompanyClientUseCase useCase;

    public CompanyClientController(CompanyClientUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CompanyClient client) {
        try {
            useCase.registerCompanyClient(client);
            return new ResponseEntity<>("Empresa registrada exitosamente", HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}