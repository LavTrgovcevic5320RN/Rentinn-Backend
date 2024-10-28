package rs.edu.raf.rentinn.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import rs.edu.raf.rentinn.requests.CustomerActivationRequest;
import rs.edu.raf.rentinn.requests.CustomerCreationRequest;
import rs.edu.raf.rentinn.requests.EditCustomerRequest;
import rs.edu.raf.rentinn.requests.EditFavoritePropertyRequest;
import rs.edu.raf.rentinn.responses.CustomerResponse;

public interface CustomerService extends UserDetailsService {

    boolean registerCustomer(CustomerCreationRequest creationRequest);

    boolean activateCustomer(CustomerActivationRequest activationRequest);

    CustomerResponse findByJwt();

    CustomerResponse findByEmail(String email);

    boolean editCustomer(EditCustomerRequest editCustomerRequest);

    boolean editFavoriteProperties(EditFavoritePropertyRequest editFavoritePropertyRequest);

}
