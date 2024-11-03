package rs.edu.raf.rentinn.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.requests.CustomerActivationRequest;
import rs.edu.raf.rentinn.requests.CustomerCreationRequest;
import rs.edu.raf.rentinn.requests.EditCustomerRequest;
import rs.edu.raf.rentinn.requests.EditFavoritePropertyRequest;
import rs.edu.raf.rentinn.responses.CustomerResponse;

public interface CustomerService extends UserDetailsService {

    void registerCustomer(CustomerCreationRequest creationRequest);

    boolean activateCustomer(CustomerActivationRequest activationRequest);

    CustomerResponse fetchCustomerByJwt();

    Customer findByJwt();

    Customer findByEmail(String email);

    boolean editCustomer(EditCustomerRequest editCustomerRequest);

    boolean editFavoriteProperties(EditFavoritePropertyRequest editFavoritePropertyRequest);

}
