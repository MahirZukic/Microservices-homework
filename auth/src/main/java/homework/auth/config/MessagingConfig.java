package homework.auth.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class MessagingConfig {

    @Value("${userservice.customerqueue}")
    public static final String USER_CREATED_QUEUE_LOANSERVICE = "userCreatedQueueLoanService";

    @Value("${userservice.loanqueue}")
    public static final String USER_CREATED_QUEUE_CUSTOMERSERVICE = "userCreatedQueueCustomerService";

    @Value("${userservice.loanqueue}")
    public static final String USER_CREATED_EXCHANGE = "userCreatedExchange";


    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(USER_CREATED_EXCHANGE);
    }

    @Bean
    public ApplicationRunner runner(RabbitListenerEndpointRegistry registry,
                                    RabbitTemplate template,
                                    AmqpAdmin admin) {
        return args -> {
            Queue userCreatedQueueCustomerService = new Queue(USER_CREATED_QUEUE_CUSTOMERSERVICE, true);
            admin.declareQueue(userCreatedQueueCustomerService);
            Queue userCreatedQueueLoanService = new Queue(USER_CREATED_QUEUE_LOANSERVICE, true);
            admin.declareQueue(userCreatedQueueLoanService);
            admin.declareBinding(
                    BindingBuilder.bind(userCreatedQueueCustomerService)
                            .to(directExchange())
                            .with("customer")
            );
            admin.declareBinding(
                    BindingBuilder.bind(userCreatedQueueLoanService)
                            .to(directExchange())
                            .with("loan")
            );
            registry.start();
        };
    }
}
