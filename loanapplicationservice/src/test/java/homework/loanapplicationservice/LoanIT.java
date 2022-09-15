package homework.loanapplicationservice;

import com.google.gson.Gson;
import homework.loanapplicationservice.entity.CustomerEntity;
import homework.loanapplicationservice.model.LoanDTO;
import homework.loanapplicationservice.model.LoanStatus;
import homework.loanapplicationservice.repository.CustomerRepository;
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
public class LoanIT {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoanCreateResource() throws Exception {
        //given
        LoanDTO inputLoanDTO = new LoanDTO(null, 1, 10.2, 1, LoanStatus.APPLIED);
        CustomerEntity customerEntity
                = new CustomerEntity(1, "test", "test", "test12", "test@test.com");
        customerRepository.save(customerEntity);

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/loanapplications")
                .content(new Gson().toJson(inputLoanDTO))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        //then
        assertNotNull(contentAsString);
        LoanDTO resultLoanDTO = new Gson().fromJson(contentAsString, LoanDTO.class);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(1, resultLoanDTO.getCustomerId());
        assertEquals(10.2, resultLoanDTO.getAmount());
        assertEquals(1, resultLoanDTO.getDuration());
        assertEquals(LoanStatus.APPLIED, resultLoanDTO.getStatus());
    }
}
