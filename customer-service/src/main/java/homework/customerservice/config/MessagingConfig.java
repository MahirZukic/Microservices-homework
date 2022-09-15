package homework.customerservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class MessagingConfig {
    @Value("${customerservice.queue}")
    public static final String CUSTOMER_CREATED_QUEUE = "customerCreatedQueue";

    @Value("${customerservice.exchange}")
    public static final String CUSTOMER_CREATED_EXCHANGE = "customerCreatedExchange";

    @Value("${customerservice.routingkey}")
    public static final String ROUTING_KEY = "customer.created";

    @Value("${customerservice.userqueue}")
    public static final String USER_CREATED_QUEUE = "userCreatedQueueCustomerService";

    @Bean
    Queue customerCreatedQueue() {
        return new Queue(CUSTOMER_CREATED_QUEUE, true);
    }

    @Bean
    Queue userCreatedQueue() {
        return new Queue(USER_CREATED_QUEUE, true);
    }

    @Bean
    TopicExchange customerCreatedExchange() {
        return new TopicExchange(CUSTOMER_CREATED_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue customerCreatedQueue, TopicExchange customerCreatedExchange) {
        return BindingBuilder
                .bind(customerCreatedQueue)
                .to(customerCreatedExchange)
                .with(ROUTING_KEY);
    }
}
