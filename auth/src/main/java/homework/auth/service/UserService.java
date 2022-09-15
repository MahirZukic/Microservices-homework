package homework.auth.service;

import com.google.gson.Gson;
import homework.auth.entity.UserEntity;
import homework.auth.exception.UserAlreadyFoundException;
import homework.auth.exception.UserNotFoundException;
import homework.auth.model.UserDTO;
import homework.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static homework.auth.config.MessagingConfig.USER_CREATED_QUEUE_CUSTOMERSERVICE;
import static homework.auth.config.MessagingConfig.USER_CREATED_QUEUE_LOANSERVICE;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RabbitTemplate template;
    private final DirectExchange directExchange;

    public UserDTO create(UserDTO userDTO) {
        log.info("create: Entering userDTO.getUsername(): {}", userDTO.getUsername());
        Optional<UserEntity> optionalPrevUser = userRepository.findByUsername(userDTO.getUsername());
        if (optionalPrevUser.isPresent()) {
            throw new UserAlreadyFoundException("User already present.");
        }
        userDTO.setId(UUID.randomUUID().toString());
        userDTO.setRoles("ROLE_USER");
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));

        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(userDTO, user);
        userRepository.save(user);
        log.info("Saved user with id: {}", user.getId());
        String json = new Gson().toJson(userDTO);
        template.convertAndSend(directExchange.getName(), "customer", json);
        log.info("User Service: Message sent to queue {}", USER_CREATED_QUEUE_CUSTOMERSERVICE);

        template.convertAndSend(directExchange.getName(), "loan", json);
        log.info("User Service: Message sent to queue {}", USER_CREATED_QUEUE_LOANSERVICE);

        userDTO.setPassword(null);
        return userDTO;
    }

    public UserDTO get(String id) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException();
        }
        UserDTO userDTO = new UserDTO();
        UserEntity user = optionalUser.get();
        log.info("Found user with id: {}", user.getId());
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}
