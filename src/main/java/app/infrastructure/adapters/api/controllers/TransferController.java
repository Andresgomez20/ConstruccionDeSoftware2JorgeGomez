package app.infrastructure.adapters.api.controllers;

import app.application.usecases.TransferUseCase;
import app.domain.models.entities.Transfer;
import app.domain.Exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferUseCase transferUseCase;

    public TransferController(TransferUseCase transferUseCase) {
        this.transferUseCase = transferUseCase;
    }

    // Endpoint para crear una transferencia
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Transfer transfer, @RequestParam boolean isCompanyUser) {
        try {
            transferUseCase.createTransfer(transfer, isCompanyUser); // <-- Ahora enviamos el boolean
            return new ResponseEntity<>("Transferencia creada exitosamente", HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para aprobar una transferencia
 
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id, 
                                     @RequestParam boolean isCompanyUser, 
                                     @RequestParam Long analystId) { // Cambiado a Long y añadido el boolean
        try {
            transferUseCase.approveTransfer(id, isCompanyUser, analystId); 
            return ResponseEntity.ok("Transferencia aprobada");
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint para expirar una transferencia (esto lo llamaría un proceso automático)
    @PutMapping("/{id}/expire")
    public ResponseEntity<?> expire(@PathVariable Long id) {
        try {
            transferUseCase.expireTransfer(id);
            return ResponseEntity.ok("Transferencia expirada");
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}