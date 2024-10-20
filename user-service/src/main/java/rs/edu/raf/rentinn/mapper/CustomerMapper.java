package rs.edu.raf.rentinn.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.model.Property;
import rs.edu.raf.rentinn.repositories.PermissionRepository;
import rs.edu.raf.rentinn.requests.CustomerCreationRequest;
import rs.edu.raf.rentinn.responses.CustomerResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {
    private PermissionMapper permissionMapper;
    private PropertyMapper propertyMapper;
    private PasswordEncoder passwordEncoder;
    private PermissionRepository permissionRepository;

    public CustomerMapper(PermissionMapper permissionMapper, PropertyMapper propertyMapper) {
        this.permissionMapper = permissionMapper;
        this.propertyMapper = propertyMapper;
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

    public CustomerResponse customerToCustomerResponse(Customer customer) {
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setId(customer.getUserId());
        customerResponse.setFirstName(customer.getFirstName());
        customerResponse.setLastName(customer.getLastName());
        customerResponse.setEmail(customer.getEmail());
        customerResponse.setPermissions(customer.getPermissions()
                .stream()
                .map(permissionMapper::permissionToPermissionDto)
                .collect(Collectors.toList())
        );

        if(customer.getProperties() != null) {
            customerResponse.setProperties(customer.getProperties()
                    .stream()
                    .map(propertyMapper::propertyToPropertyDto)
                    .collect(Collectors.toList())
            );
        } else {
            customerResponse.setProperties(new ArrayList<>());
        }

        return customerResponse;
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
