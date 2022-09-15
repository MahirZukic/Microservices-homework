package homework.loanapplicationservice.model;

import lombok.Data;

import java.util.List;

@Data
public class CustomerLoanDTO {
    private CustomerDTO customer;
    private List<LoanDTO> loanDTOs;
}
