package homework.loanapplicationservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class MessagingConfig {
    @Value("${loanservice.customerqueue}")
    public static final String CUSTOMER_CREATED_QUEUE = "customerCreatedQueue";

    @Value("${loanservice.userqueue}")
    public static final String USER_CREATED_QUEUE = "userCreatedQueueLoanService";

    @Bean
    Queue customerCreatedQueue() {
        return new Queue(CUSTOMER_CREATED_QUEUE, true);
    }

    @Bean
    Queue userCreatedQueue() {
        return new Queue(USER_CREATED_QUEUE, true);
    }
}
