package homework.customerservice.customerservice;

import com.google.gson.Gson;
import homework.customerservice.entity.UserEntity;
import homework.customerservice.model.CustomerDTO;
import homework.customerservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoanCreateResource() throws Exception {
        //given
        CustomerDTO inputCustomerDTO = new CustomerDTO(null,"1","firstname","lastname","test@test.com","1234");
        UserEntity userEntity
                = new UserEntity("1","username", "password",null);
        userRepository.save(userEntity);

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                .content(new Gson().toJson(inputCustomerDTO))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        //then
        assertNotNull(contentAsString);
        CustomerDTO resultCustomerDTO = new Gson().fromJson(contentAsString, CustomerDTO.class);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("1", resultCustomerDTO.getUserId());
        assertEquals("firstname", resultCustomerDTO.getFirstName());
        assertEquals("lastname", resultCustomerDTO.getLastName());
        assertEquals("test@test.com", resultCustomerDTO.getEmail());
        assertEquals("1234", resultCustomerDTO.getPhone());
    }
}
