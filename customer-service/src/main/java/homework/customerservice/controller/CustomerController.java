package homework.customerservice.controller;

import homework.customerservice.model.CustomerDTO;
import homework.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> create(
            @RequestBody @Valid CustomerDTO customerDTO) {
        log.info("CustomerController: Entering create customer: {}", customerDTO);
        return ResponseEntity.ok().body(customerService.create(customerDTO));
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerDTO> get(
            @PathVariable("id") String id) {
        log.info("CustomerController: Entering get customer: {}", id);
        return ResponseEntity.ok().body(customerService.get(id));
    }
}
