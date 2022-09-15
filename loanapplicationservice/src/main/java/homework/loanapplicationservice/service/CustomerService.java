package homework.loanapplicationservice.service;


import com.google.gson.Gson;
import homework.loanapplicationservice.entity.CustomerEntity;
import homework.loanapplicationservice.model.CustomerDTO;
import homework.loanapplicationservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    public static final String CUSTOMER_CREATED_QUEUE = "customerCreatedQueue";
    private final CustomerRepository customerRepository;

    @RabbitListener(queues = CUSTOMER_CREATED_QUEUE)
    public void consumeCustomerCreated(String consumedString) {
        log.info("Received customer created: {}", consumedString);
        CustomerDTO customerDTO = new Gson().fromJson(consumedString, CustomerDTO.class);
        saveCustomer(customerDTO);
    }

    public void saveCustomer(CustomerDTO customerDTO) {
        if (!customerRepository.findById(customerDTO.getId()).isPresent()) {
            CustomerEntity customerEntity = new CustomerEntity();
            BeanUtils.copyProperties(customerDTO, customerEntity);
            customerRepository.save(customerEntity);
        }
    }
}
