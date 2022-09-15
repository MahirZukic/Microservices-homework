package homework.loanapplicationservice.service;

import com.google.gson.Gson;
import homework.loanapplicationservice.entity.UserEntity;
import homework.loanapplicationservice.model.UserDTO;
import homework.loanapplicationservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static homework.loanapplicationservice.config.MessagingConfig.USER_CREATED_QUEUE;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @RabbitListener(queues = USER_CREATED_QUEUE)
    public void consumeUserCreated(String consumedString) {
        log.info("Message received in user created");
        UserDTO userDTO = new Gson().fromJson(consumedString, UserDTO.class);
        saveUser(userDTO);
    }

    public void saveUser(UserDTO userDTO) {
        if (!userRepository.findById(userDTO.getId()).isPresent()) {
            UserEntity userEntity = new UserEntity();
            BeanUtils.copyProperties(userDTO, userEntity);
            userEntity = userRepository.save(userEntity);
            log.info("Saved user with id: {}", userEntity.getId());
        }
    }
}
