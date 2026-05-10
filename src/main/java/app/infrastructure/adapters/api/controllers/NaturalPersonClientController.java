package app.infrastructure.adapters.api.controllers;

import app.application.usecases.NaturalPersonClientUseCase;
import app.domain.models.entities.NaturalPersonClient;
import app.domain.Exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
public class NaturalPersonClientController {

    private final NaturalPersonClientUseCase useCase;

    public NaturalPersonClientController(NaturalPersonClientUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody NaturalPersonClient client) {
        try {
            useCase.registerNaturalPersonClient(client);
            return new ResponseEntity<>("Persona registrada exitosamente", HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}