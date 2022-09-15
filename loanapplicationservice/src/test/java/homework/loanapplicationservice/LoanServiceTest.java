package homework.loanapplicationservice;

import homework.loanapplicationservice.entity.CustomerEntity;
import homework.loanapplicationservice.entity.LoanEntity;
import homework.loanapplicationservice.exception.InvalidLoanRequestException;
import homework.loanapplicationservice.model.LoanDTO;
import homework.loanapplicationservice.model.LoanStatus;
import homework.loanapplicationservice.repository.CustomerRepository;
import homework.loanapplicationservice.repository.LoanRepository;
import homework.loanapplicationservice.service.LoanService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;

public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository = Mockito.mock(LoanRepository.class);

    @Mock
    private CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);

    private LoanService loanServiceUnderTest;

    public LoanServiceTest() {
        loanServiceUnderTest = new LoanService(loanRepository, customerRepository);
    }

    @ParameterizedTest
    @MethodSource("getLoanDTOsNegative")
    void withCustomConverterNegative(LoanDTO loanDTO) {
        assertThrows(InvalidLoanRequestException.class,
                () -> loanServiceUnderTest.create(loanDTO));
    }

    @ParameterizedTest
    @MethodSource("getLoanDTOsPositive")
    void withCustomConverterPositive(LoanDTO loanDTO) {
        Optional<CustomerEntity> customer = Optional.of(new CustomerEntity(1, "First", "Last", "phone", "email"));
        Mockito.when(customerRepository.findById(loanDTO.getCustomerId())).thenReturn(customer);
        Mockito.when(loanRepository.save(any(LoanEntity.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0) );
        LoanDTO loanDTO1 = loanServiceUnderTest.create(loanDTO);
        assertNotNull(loanDTO1);
    }


    private static Stream<Arguments> getLoanDTOsNegative() {
        return Stream.of(
                arguments(new LoanDTO("testid", 1, -10.2, 1, LoanStatus.APPLIED)),
                arguments(new LoanDTO("testid", 1, 10.2, -1, LoanStatus.APPLIED)));
    }


    private static Stream<Arguments> getLoanDTOsPositive() {
        return Stream.of(
                arguments(new LoanDTO("testid", 1, 10000.0, 74, LoanStatus.APPLIED)),
                arguments(new LoanDTO("testid", 1, 15000.0, 120, LoanStatus.APPLIED)));
    }
}
