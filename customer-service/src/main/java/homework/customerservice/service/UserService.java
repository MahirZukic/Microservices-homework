package homework.customerservice.service;

import com.google.gson.Gson;
import homework.customerservice.entity.UserEntity;
import homework.customerservice.model.UserDTO;
import homework.customerservice.repository.UserRepository;
import homework.customerservice.config.MessagingConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @RabbitListener(queues = MessagingConfig.USER_CREATED_QUEUE)
    public void consumeUserCreated(String consumedString) {
        log.info("Message received in user created");
        UserDTO userDTO = new Gson().fromJson(consumedString, UserDTO.class);
        saveUser(userDTO);
    }

    public void saveUser(UserDTO userDTO) {
        try {
            if (!userRepository.findById(userDTO.getId()).isPresent()) {
                UserEntity userEntity = new UserEntity();
                BeanUtils.copyProperties(userDTO, userEntity);
                userEntity = userRepository.save(userEntity);
                log.info("Saved user with id: {}", userEntity.getId());
            }
        } catch(Exception e) {
            throw new IllegalStateException();
        }
    }
}
