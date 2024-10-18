package rs.edu.raf.rentinn.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import rs.edu.raf.rentinn.requests.CustomerActivationRequest;
import rs.edu.raf.rentinn.requests.CustomerCreationRequest;

public interface CustomerService extends UserDetailsService {

    boolean registerCustomer(CustomerCreationRequest creationRequest);

    boolean activateCustomer(CustomerActivationRequest activationRequest);
}
