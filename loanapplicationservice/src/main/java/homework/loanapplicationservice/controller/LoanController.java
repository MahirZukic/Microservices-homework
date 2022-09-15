package homework.loanapplicationservice.controller;

import homework.loanapplicationservice.model.CustomerLoanDTO;
import homework.loanapplicationservice.model.LoanDTO;
import homework.loanapplicationservice.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/loanapplications")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanDTO> create(
            @RequestBody @Valid LoanDTO loanDTO) {
        log.info("LoanController: Entering create loan: {}", loanDTO);
        return ResponseEntity.ok().body(loanService.create(loanDTO));
    }

    @GetMapping
    public ResponseEntity<CustomerLoanDTO> get(
            @RequestParam("customerId") String customerId) {
        log.info("LoanController: Entering get loan for customer: {}", customerId);
        return ResponseEntity.ok().body(loanService.get(customerId));
    }
}
