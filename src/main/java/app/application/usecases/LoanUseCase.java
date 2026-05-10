package app.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exceptions.BusinessException;
import app.domain.services.DisburseLoan;
import app.domain.services.RejectLoan;
// Importa aquí tus otros servicios de préstamos como RequestLoan y ApproveLoan cuando los tengas listos

@Service
public class LoanUseCase {

    @Autowired
    private DisburseLoan disburseLoan;
    
    @Autowired
    private RejectLoan rejectLoan;

    // Constructor con inyección de dependencias (igual al de la clínica)
    public LoanUseCase(DisburseLoan disburseLoan, RejectLoan rejectLoan) {
        this.disburseLoan = disburseLoan;
        this.rejectLoan = rejectLoan;
    }

    // ==========================================
    // MÉTODOS DEL CASO DE USO (Fachada)
    // ==========================================

    public void disburseLoan(Long loanId, String analystUserId) throws BusinessException {
        disburseLoan.execute(loanId, analystUserId);
    }

    public void rejectLoan(Long loanId, String analystUserId, String reason) throws BusinessException {
        rejectLoan.execute(loanId, analystUserId, reason);
    }
    
    // Aquí puedes agregar los métodos para requestLoan y approveLoan en el futuro
}