package rs.edu.raf.rentinn.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.model.Employee;
import rs.edu.raf.rentinn.model.Permission;
import rs.edu.raf.rentinn.model.User;
import rs.edu.raf.rentinn.repositories.CustomerRepository;
import rs.edu.raf.rentinn.repositories.EmployeeRepository;
import rs.edu.raf.rentinn.repositories.PermissionRepository;
import rs.edu.raf.rentinn.utils.Constants;

import java.util.HashSet;

@Component
public class Bootstrap implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;


    @Autowired
    public Bootstrap(
            PermissionRepository permissionRepository,
            PasswordEncoder passwordEncoder,
            EmployeeRepository employeeRepository,
            CustomerRepository customerRepository) {
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedPermissions();
        System.out.println("Permissions seeded");

        Employee admin = generateEmployee(
                "admin",
                "admin",
                "Lav",
                "Trgovcevic",
                Constants.ADMIN,
                true
        );

        Customer customerCompany = new Customer();
        customerCompany.setFirstName("Customer");
        customerCompany.setEmail("customer@gmail.com");
        customerCompany.setPassword(passwordEncoder.encode("customer"));
        customerCompany.setLastName("Markovic");
        customerCompany.setActive(true);
        customerRepository.save(customerCompany);

        System.out.println("Users seeded");
    }

    private void seedPermissions() {
        for(String s : Constants.allPermissions) {
            if(permissionRepository.findByName(s).isPresent()) {
                continue;
            }

            Permission permission = new Permission();
            permission.setName(s);
            permission.setDescription(s);
            permissionRepository.save(permission);
        }
    }

    private Employee generateEmployee(final String email, final String password, final String firstName, final String lastName, final String position, final Boolean active){
        Employee employee = new Employee();
        employee.setEmail(email);
        employee.setPassword(passwordEncoder.encode(password));
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setPosition(position);
        employee.setActive(active);
        employee.setPermissions(new HashSet<>(permissionRepository.findAll()));
        if (employeeRepository.findByEmail(employee.getEmail()).isEmpty()) {
            employeeRepository.save(employee);
        }else {
            employee = employeeRepository.findByEmail(employee.getEmail()).get();
        }
        return employee;
    }
}
