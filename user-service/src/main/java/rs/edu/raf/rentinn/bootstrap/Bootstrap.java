package rs.edu.raf.rentinn.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.model.Employee;
import rs.edu.raf.rentinn.model.Permission;
import rs.edu.raf.rentinn.model.Property;
import rs.edu.raf.rentinn.repositories.CustomerRepository;
import rs.edu.raf.rentinn.repositories.EmployeeRepository;
import rs.edu.raf.rentinn.repositories.PermissionRepository;
import rs.edu.raf.rentinn.repositories.PropertyRepository;
import rs.edu.raf.rentinn.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

@Component
public class Bootstrap implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final PropertyRepository propertyRepository;


    @Autowired
    public Bootstrap(
            PermissionRepository permissionRepository,
            PasswordEncoder passwordEncoder,
            EmployeeRepository employeeRepository,
            CustomerRepository customerRepository,
            PropertyRepository propertyRepository) {
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedPermissions();
        System.out.println("Permissions seeded");

        seedUsers();
        System.out.println("Users seeded");

        seedProperties();
        System.out.println("Properties seeded");
    }

    private void seedProperties() {
        Property property1 = new Property();
        property1.setName("Riviera Superior");
//        property2.setDescription("Description " + i);
        property1.setCountry("Greece");
        property1.setCity("Crete");
        property1.setAddress("Chania Old Town");
        property1.setPostalCode("73132");
        property1.setLatitude("35.5184506");
        property1.setLongitude("24.0226517");
        property1.setImagePaths(new ArrayList<>());
        propertyRepository.save(property1);

        Property property2 = new Property();
        property2.setName("Riviera Deluxe");
//        property2.setDescription("Description " + i);
        property2.setCountry("Greece");
        property2.setCity("Crete");
        property2.setAddress("Chania Old Town");
        property2.setPostalCode("73132");
        property2.setLatitude("35.5184506");
        property2.setLongitude("24.0226517");
        property2.setImagePaths(new ArrayList<>());
        propertyRepository.save(property2);

        Property property3 = new Property();
        property3.setName("Riviera Executive");
//        property3.setDescription("Description " + i);
        property3.setCountry("Greece");
        property3.setCity("Crete");
        property3.setAddress("Chania Old Town");
        property3.setPostalCode("73132");
        property3.setLatitude("35.5184506");
        property3.setLongitude("24.0226517");
        property3.setImagePaths(new ArrayList<>());
        propertyRepository.save(property3);

        Property property4 = new Property();
        property4.setName("Riviera Classic");
//        property4.setDescription("Description " + i);
        property4.setCountry("Greece");
        property4.setCity("Crete");
        property4.setAddress("Chania Old Town");
        property4.setPostalCode("73132");
        property4.setLatitude("35.5184506");
        property4.setLongitude("24.0226517");
        property4.setImagePaths(new ArrayList<>());
        propertyRepository.save(property4);

        Property property5 = new Property();
        property5.setName("Sagara Candidasa Classic");
//        property5.setDescription("Description " + i);
        property5.setCountry("Indonesia");
        property5.setCity("Bali");
        property5.setAddress("Candidasa");
        property5.setPostalCode("80851");
        property5.setLatitude("-8.5100184");
        property5.setLongitude("115.5704357");
        property5.setImagePaths(new ArrayList<>());
        propertyRepository.save(property5);

        Property property6 = new Property();
        property6.setName("Sagara Candidasa Premiere");
//        property6.setDescription("Description " + i);
        property6.setCountry("Indonesia");
        property6.setCity("Bali");
        property6.setAddress("Candidasa");
        property6.setPostalCode("80851");
        property6.setLatitude("-8.5100184");
        property6.setLongitude("115.5704357");
        property6.setImagePaths(new ArrayList<>());
        propertyRepository.save(property6);

        Property property7 = new Property();
        property7.setName("Genggong");
//        property7.setDescription("Description " + i);
        property7.setCountry("Indonesia");
        property7.setCity("Bali");
        property7.setAddress("Candidasa");
        property7.setPostalCode("80851");
        property7.setLatitude("-8.513122693684073");
        property7.setLongitude("115.5761964819635");
        property7.setImagePaths(new ArrayList<>());
        propertyRepository.save(property7);

        Property property8 = new Property();
        property8.setName("Sagara Candidasa Suite");

    }

    private void seedUsers() {
        for(int i = 0; i < 10; i++) {
            Employee employee = generateEmployee(
                    STR."admin\{i}@gmail.com",
                    "Markovic01@",
                    "Employee",
                    "Markovic",
                    Constants.ADMIN,
                    true
            );

            Customer customer = generateCustomer(
                    "customer" + i + "@gmail.com",
                    "Markovic01@",
                    "Customer",
                    "Markovic",
                    Constants.RENTER,
                    true
            );
        }
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
        employee.setActive(active);
        employee.setPermissions(new HashSet<>(permissionRepository.findAll()));
        if (employeeRepository.findByEmail(employee.getEmail()).isEmpty()) {
            employeeRepository.save(employee);
        }else {
            employee = employeeRepository.findByEmail(employee.getEmail()).get();
        }
        return employee;
    }

    private Customer generateCustomer(final String email, final String password, final String firstName, final String lastName, final String position, final Boolean active){
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setActive(active);
        customer.setPermissions(new HashSet<>(permissionRepository.findAll()));
        if (customerRepository.findByEmail(customer.getEmail()).isEmpty()) {
            customerRepository.save(customer);
        }else {
            customer = customerRepository.findByEmail(customer.getEmail()).get();
        }
        return customer;
    }
}
