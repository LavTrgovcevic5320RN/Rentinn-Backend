package rs.edu.raf.rentinn.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.edu.raf.rentinn.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String username);
//    Optional<Employee> findByActivationToken(String activationToken);
//    Optional<Employee> findByResetPasswordToken(String token);
//
//    @Transactional
//    @Modifying
//    @Query("update Employee u set u.active = false where u.userId = :userId")
//    void deactivateUser(@Param("userId") Long userId);
}
