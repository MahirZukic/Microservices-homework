package homework.loanapplicationservice.repository;

import homework.loanapplicationservice.entity.CustomerEntity;
import homework.loanapplicationservice.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<LoanEntity, String> {
    List<LoanEntity> findByCustomer(CustomerEntity customerEntity);
}
