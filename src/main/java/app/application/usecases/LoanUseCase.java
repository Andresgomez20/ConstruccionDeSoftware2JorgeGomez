package app.application.usecases;

import org.springframework.stereotype.Service;
import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.Loan;
import app.domain.services.ApproveLoan;
import app.domain.services.DisburseLoan;
import app.domain.services.RejectLoan;
import app.domain.services.RequestLoan; 

@Service
public class LoanUseCase {

    private final ApproveLoan approveLoan;
    private final DisburseLoan disburseLoan;
    private final RejectLoan rejectLoan;
    private final RequestLoan requestLoan; 

    public LoanUseCase(ApproveLoan approveLoan, DisburseLoan disburseLoan, RejectLoan rejectLoan, RequestLoan requestLoan) {
        this.approveLoan = approveLoan;
        this.disburseLoan = disburseLoan;
        this.rejectLoan = rejectLoan;
        this.requestLoan = requestLoan;
    }

    
    public void requestLoan(Loan loan) throws BusinessException {
        requestLoan.request(loan);
    }

    public void approveLoan(Long loanId) throws BusinessException {
        approveLoan.execute(loanId);
    }

    public void rejectLoan(Long loanId, String reason) throws BusinessException {
        rejectLoan.execute(loanId, reason);
    }

    public void disburseLoan(Long loanId) throws BusinessException {
        disburseLoan.execute(loanId);
    }
}