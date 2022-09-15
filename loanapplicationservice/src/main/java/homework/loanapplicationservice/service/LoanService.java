package homework.loanapplicationservice.service;


import homework.loanapplicationservice.entity.CustomerEntity;
import homework.loanapplicationservice.entity.LoanEntity;
import homework.loanapplicationservice.exception.CustomerNotFoundException;
import homework.loanapplicationservice.exception.InvalidLoanRequestException;
import homework.loanapplicationservice.model.CustomerDTO;
import homework.loanapplicationservice.model.CustomerLoanDTO;
import homework.loanapplicationservice.model.LoanDTO;
import homework.loanapplicationservice.repository.CustomerRepository;
import homework.loanapplicationservice.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public LoanDTO create(LoanDTO loanDTO) {
        validate(loanDTO);
        Optional<CustomerEntity> customerEntityOptional = getCustomerEntity(loanDTO.getCustomerId());
        LoanEntity loanEntity = getLoanEntity(loanDTO, customerEntityOptional);
        BeanUtils.copyProperties(loanEntity, loanDTO);
        return loanDTO;
    }


    public CustomerLoanDTO get(String customerId) {
        Optional<CustomerEntity> customerEntityOptional = getCustomerEntity(Integer.valueOf(customerId));
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customerEntityOptional.get(), customerDTO);
        List<LoanEntity> loanEntities = loanRepository.findByCustomer(customerEntityOptional.get());
        List<LoanDTO> loanDTOS = new ArrayList<>();
        for (LoanEntity loanEntity : loanEntities) {
            loanDTOS.add(getLoanDTO(loanEntity));
        }
        return getCustomerLoanDTO(customerDTO, loanDTOS);
    }

    private CustomerLoanDTO getCustomerLoanDTO(CustomerDTO customerDTO, List<LoanDTO> loanDTOS) {
        CustomerLoanDTO customerLoanDTO = new CustomerLoanDTO();
        customerLoanDTO.setCustomer(customerDTO);
        customerLoanDTO.setLoanDTOs(loanDTOS);
        return customerLoanDTO;
    }

    private LoanDTO getLoanDTO(LoanEntity loanEntity) {
        LoanDTO loanDTO = new LoanDTO();
        BeanUtils.copyProperties(loanEntity, loanDTO);
        loanDTO.setCustomerId(loanEntity.getCustomer().getId());
        return loanDTO;
    }

    private void validate(LoanDTO loanDTO) {
        if (Double.compare(loanDTO.getAmount(), 0.0) < 0) {
            throw new InvalidLoanRequestException();
        }
        if (loanDTO.getDuration() < 0) {
            throw new InvalidLoanRequestException();
        }
    }

    private Optional<CustomerEntity> getCustomerEntity(Integer id) {
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(id);
        if (!customerEntityOptional.isPresent()) {
            log.error("LoanService: Customer not found with id {}", id);
            throw new CustomerNotFoundException();
        }
        return customerEntityOptional;
    }

    private LoanEntity getLoanEntity(LoanDTO loanDTO, Optional<CustomerEntity> customerEntityOptional) {
        loanDTO.setId(UUID.randomUUID().toString());
        LoanEntity loanEntity = new LoanEntity();
        BeanUtils.copyProperties(loanDTO, loanEntity);
        loanEntity.setCustomer(customerEntityOptional.get());
        loanEntity = loanRepository.save(loanEntity);
        log.info("Saved loan with id: {}", loanEntity.getId());
        return loanEntity;
    }
}
