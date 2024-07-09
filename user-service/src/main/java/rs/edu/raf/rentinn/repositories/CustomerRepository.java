package rs.edu.raf.rentinn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.rentinn.model.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findCustomerByActivationToken(String token);
    Optional<Customer> findCustomerByEmail(String email);
    Optional<Customer> findByUserId(Long userId);
    Optional<Customer> findByResetPasswordToken(String token);

}
