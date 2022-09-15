package homework.customerservice.customerservice;

import homework.customerservice.exception.InvalidCustomerRequestException;
import homework.customerservice.model.CustomerDTO;
import homework.customerservice.repository.CustomerRepository;
import homework.customerservice.repository.UserRepository;
import homework.customerservice.service.CustomerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CustomerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private TopicExchange topicExchange;


    private CustomerService classUnderTest;

    public CustomerServiceTest() {
        classUnderTest = new CustomerService(userRepository, customerRepository, rabbitTemplate, topicExchange);
    }

    @ParameterizedTest
    @MethodSource("getCustomerDTOs")
    void withCustomConverter(CustomerDTO customerDTO) {
        assertThrows(InvalidCustomerRequestException.class,
                () -> classUnderTest.create(customerDTO));
    }

    private static Stream<Arguments> getCustomerDTOs() {
        return Stream.of(
                arguments(new CustomerDTO(1,"1","Invalidfirstname123","validlastname","test@test.com","1234")),
                arguments(new CustomerDTO(1,"1","validfirstname","Invalidlastname123","test@test.com","1234")));
    }
}
