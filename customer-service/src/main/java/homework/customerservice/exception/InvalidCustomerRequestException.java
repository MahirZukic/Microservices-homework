package homework.customerservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidCustomerRequestException extends RuntimeException {
  private static final long serialVersionUID = -2453166221529927594L;
}
