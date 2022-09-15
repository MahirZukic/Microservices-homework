package homework.loanapplicationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDTO {
    private String id;
    @NotNull
    private Integer customerId;
    @NotNull
    private Double amount;
    @NotNull
    private Integer duration;
    @NotNull
    private LoanStatus status;
}
