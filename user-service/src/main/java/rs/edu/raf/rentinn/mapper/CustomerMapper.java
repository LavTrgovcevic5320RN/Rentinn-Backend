package rs.edu.raf.rentinn.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.repositories.PermissionRepository;
import rs.edu.raf.rentinn.requests.CustomerCreationRequest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {
    private PermissionMapper permissionMapper;

    private PasswordEncoder passwordEncoder;

    private PermissionRepository permissionRepository;

    public CustomerMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public Customer createNewCustomer(CustomerCreationRequest createCustomerRequest){
        Customer customer = new Customer();
        customer.setFirstName(createCustomerRequest.getFirstName());
        customer.setLastName(createCustomerRequest.getLastName());
        customer.setEmail(createCustomerRequest.getEmail());
        customer.setPassword(passwordEncoder.encode(createCustomerRequest.getPassword()));
        customer.setGender(createCustomerRequest.getGender());
        customer.setDateOfBirth(createCustomerRequest.getDateOfBirth());
        customer.setNationality(createCustomerRequest.getNationality());
        customer.setPhoneNumber(createCustomerRequest.getPhoneNumber());
        customer.setActive(false);
        customer.setActivationToken(UUID.randomUUID().toString());
        return customer;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
}
