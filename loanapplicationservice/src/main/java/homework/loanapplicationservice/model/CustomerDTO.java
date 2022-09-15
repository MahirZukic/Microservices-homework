package homework.loanapplicationservice.model;

import lombok.Data;

@Data
public class CustomerDTO{
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
