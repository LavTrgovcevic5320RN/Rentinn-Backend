package rs.edu.raf.rentinn.services.implementations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.edu.raf.rentinn.dtos.EmployeeDto;
import rs.edu.raf.rentinn.mapper.EmployeeMapper;
import rs.edu.raf.rentinn.mapper.PermissionMapper;
import rs.edu.raf.rentinn.model.Employee;
import rs.edu.raf.rentinn.repositories.EmployeeRepository;
import rs.edu.raf.rentinn.repositories.PermissionRepository;
import rs.edu.raf.rentinn.services.EmployeeService;
import rs.edu.raf.rentinn.utils.JwtUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Value("${front.port}")
    private String frontPort;
    private EmployeeMapper employeeMapper;
    private PermissionMapper permissionMapper;
    private EmployeeRepository employeeRepository;
    private PermissionRepository permissionRepository;
//    private EmailService emailService;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(
            EmployeeMapper employeeMapper,
            PermissionMapper permissionMapper,
            EmployeeRepository employeeRepository,
            PermissionRepository permissionRepository,
//            EmailService emailService,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder
    ){
        this.employeeMapper = employeeMapper;
        this.permissionMapper = permissionMapper;
        this.employeeRepository = employeeRepository;
        this.permissionRepository = permissionRepository;
//        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> myEmployee = this.employeeRepository.findByEmail(username);

        if (myEmployee.isEmpty()) {
            throw new UsernameNotFoundException("User name " + username + " not found");
        }

        Employee employee = myEmployee.get();

        List<SimpleGrantedAuthority> authorities = employee.getPermissions()
                .stream()
                .map((permission -> new SimpleGrantedAuthority(permission.getName())))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(employee.getEmail(),
                employee.getPassword(),
                authorities);
    }

    @Override
    public List<EmployeeDto> findAll() {
        return this.employeeRepository.findAll()
                .stream()
                .map(this.employeeMapper::employeeToEmployeeDto)
                .toList();
    }

}
