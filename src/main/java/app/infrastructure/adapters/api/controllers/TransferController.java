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

    /**
     * Endpoint para crear una transferencia
     * La validación de si es empresa o cliente se realiza basada en el JWT
     */
        // Ejemplo en Postman: POST /api/transfers
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Transfer transfer) {
        try {
            transferUseCase.createTransfer(transfer);
            return new ResponseEntity<>("Transferencia creada exitosamente", HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para aprobar una transferencia
     * Requiere autenticación con rol COMPANY_SUPERVISOR
     * El usuario y la validación se obtienen automáticamente del JWT
     */
    // Ejemplo: PUT /api/transfers/123/approve
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        try {
            transferUseCase.approveTransfer(id, true); // true = aprobar
            return ResponseEntity.ok("Transferencia aprobada");
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para rechazar una transferencia
     * Requiere autenticación con rol COMPANY_SUPERVISOR
     * El usuario y la validación se obtienen automáticamente del JWT
     */
    // Ejemplo: PUT /api/transfers/123/reject
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        try {
            transferUseCase.approveTransfer(id, false); // false = rechazar
            return ResponseEntity.ok("Transferencia rechazada");
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para expirar una transferencia (esto lo llamaría un proceso automático)
     */
    // Ejemplo: PUT /api/transfers/123/expire
    @PutMapping("/{id}/expire")
    public ResponseEntity<?> expire(@PathVariable Long id) {
        try {
            transferUseCase.expireTransfer(id);
            return ResponseEntity.ok("Transferencia expirada");
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}