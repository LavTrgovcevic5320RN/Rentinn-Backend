package rs.edu.raf.rentinn.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import rs.edu.raf.rentinn.dtos.EmployeeDto;

import java.util.List;

public interface EmployeeService extends UserDetailsService {
    List<EmployeeDto> findAll();
}
