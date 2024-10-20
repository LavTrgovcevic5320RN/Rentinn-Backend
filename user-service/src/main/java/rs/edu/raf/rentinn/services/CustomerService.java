package rs.edu.raf.rentinn.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import rs.edu.raf.rentinn.requests.CustomerActivationRequest;
import rs.edu.raf.rentinn.requests.CustomerCreationRequest;
import rs.edu.raf.rentinn.responses.CustomerResponse;

public interface CustomerService extends UserDetailsService {

    boolean registerCustomer(CustomerCreationRequest creationRequest);

    boolean activateCustomer(CustomerActivationRequest activationRequest);

    CustomerResponse findByJwt();

    CustomerResponse findByEmail(String email);

}
