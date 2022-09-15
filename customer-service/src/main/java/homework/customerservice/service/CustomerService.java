package homework.customerservice.service;

import com.google.gson.Gson;
import homework.customerservice.entity.CustomerEntity;
import homework.customerservice.entity.UserEntity;
import homework.customerservice.exception.CustomerNotFoundException;
import homework.customerservice.exception.InvalidCustomerRequestException;
import homework.customerservice.exception.UserNotFoundException;
import homework.customerservice.model.CustomerDTO;
import homework.customerservice.repository.CustomerRepository;
import homework.customerservice.repository.UserRepository;
import homework.customerservice.config.MessagingConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final UserRepository userRepository;

    private final CustomerRepository customerRepository;

    private final RabbitTemplate rabbitTemplate;

    @Qualifier(value = MessagingConfig.CUSTOMER_CREATED_EXCHANGE)
    private final TopicExchange topicExchange;

    @Transactional
    public CustomerDTO create(CustomerDTO customerDTO) {
        validate(customerDTO);
        Optional<UserEntity> userEntityOptional = getUserEntity(customerDTO.getUserId());
        CustomerEntity customerEntity = saveCustomerEntity(customerDTO, userEntityOptional.get());
        customerDTO.setId(customerEntity.getId());
        publishMessage(customerDTO);
        return customerDTO;
    }

    public CustomerDTO get(String id) {
        CustomerEntity customerEntity = getCustomerEntity(id);
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customerEntity, customerDTO);
        customerDTO.setUserId(customerEntity.getUser().getId());
        return customerDTO;
    }

    private CustomerEntity getCustomerEntity(String id) {
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(Integer.valueOf(id));
        if (!optionalCustomerEntity.isPresent()) {
            log.info("CustomerService: Customer not found with id {}", id);
            throw new CustomerNotFoundException();
        }
        return optionalCustomerEntity.get();
    }

    private void publishMessage(CustomerDTO customerDTO) {
        String json = new Gson().toJson(customerDTO);
        rabbitTemplate.convertAndSend(topicExchange.getName(), MessagingConfig.ROUTING_KEY, json);
        log.info("CustomerService: Message sent to topic {}", topicExchange.getName());
    }

    private CustomerEntity saveCustomerEntity(CustomerDTO customerDTO, UserEntity userEntity) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUser(userEntity);
        BeanUtils.copyProperties(customerDTO, customerEntity, "userId", "id");
        customerEntity = customerRepository.save(customerEntity);
        log.info("CustomerService: Saved customer with id: {}", customerEntity.getId());
        return customerEntity;
    }

    private Optional<UserEntity> getUserEntity(String id) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);
        if (!userEntityOptional.isPresent()) {
            log.error("CustomerService: User not found with id {}", id);
            throw new UserNotFoundException();
        }
        return userEntityOptional;
    }

    private void validate(CustomerDTO customerDTO) {
        if (customerDTO.getFirstName().matches(".*\\d+.*") ||
                customerDTO.getLastName().matches(".*\\d+.*")) {
            throw new InvalidCustomerRequestException();
        }
    }

}
