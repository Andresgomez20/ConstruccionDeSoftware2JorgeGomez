package app.domain.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GeneralBankingProduct {

    private String productCode;
    private String productName;
    private String category;
    private boolean requiresApproval;

}