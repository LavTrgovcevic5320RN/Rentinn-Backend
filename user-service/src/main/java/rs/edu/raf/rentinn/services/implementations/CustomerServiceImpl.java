package rs.edu.raf.rentinn.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.edu.raf.rentinn.exceptions.EmailAlreadyExistsException;
import rs.edu.raf.rentinn.exceptions.InvalidTokenException;
import rs.edu.raf.rentinn.mapper.CustomerMapper;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.repositories.CustomerRepository;
import rs.edu.raf.rentinn.requests.CustomerActivationRequest;
import rs.edu.raf.rentinn.requests.CustomerCreationRequest;
import rs.edu.raf.rentinn.requests.EditCustomerRequest;
import rs.edu.raf.rentinn.requests.EditFavoritePropertyRequest;
import rs.edu.raf.rentinn.responses.CustomerResponse;
import rs.edu.raf.rentinn.services.CustomerService;
import rs.edu.raf.rentinn.services.EmailService;
import rs.edu.raf.rentinn.services.EmployeeService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final EmailService emailService;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CustomerMapper customerMapper,
                               EmailService emailService) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.emailService = emailService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> myCustomer = this.customerRepository.findCustomerByEmail(username);

        if (myCustomer.isEmpty()) {
            throw new UsernameNotFoundException("Email " + username + " not found");
        }

        Customer customer = myCustomer.get();

        List<SimpleGrantedAuthority> authorities = customer.getPermissions()
                .stream()
                .map((permission -> new SimpleGrantedAuthority(permission.getName())))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(customer.getEmail(),
                customer.getPassword(),
                authorities);
    }


    public void registerCustomer(CustomerCreationRequest creationRequest) {
        if (customerRepository.findByEmail(creationRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already taken.");
        }

        Customer customer = customerMapper.createNewCustomer(creationRequest);
        customer = customerRepository.save(customer);

        String activationLink = "http://localhost:4200/activate?token=" + customer.getActivationToken();
        String sendTo = customer.getEmail();
        String subject = "Account activation";
        String text = "Please use the following token to activate your account: " + customer.getActivationToken() + "\n" + "Or click the link: " + activationLink;
        emailService.sendEmail(sendTo, subject, text);
    }


    public boolean activateCustomer(CustomerActivationRequest activationRequest) {
        Optional<Customer> optionalCustomer = customerRepository.findCustomerByEmailAndActivationToken(activationRequest.getEmail(), activationRequest.getActivationToken());
        if (optionalCustomer.isEmpty()) {
            throw new InvalidTokenException("Invalid activation token.");
        }

        Customer customer = optionalCustomer.get();
        customer.setActive(true);
        customer.setActivationToken(null); // Clear the token after activation
        customerRepository.save(customer);
        return false;
    }


    @Override
    public CustomerResponse fetchCustomerByJwt() {
        Customer customer = findByJwt();
        if (customer == null) {
            return null;
        }
        return customerMapper.customerToCustomerResponse(customer);
    }


    @Override
    public Customer findByJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null)
            return null;

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            logger.warn("Principal is not an instance of UserDetails");
            return null;
        }

        return findByEmail(userDetails.getUsername());
    }


    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findCustomerByEmail(email).orElse(null);
    }


    @Override
    public boolean editCustomer(EditCustomerRequest editCustomerRequest) {
        Optional<Customer> optionalCustomer = customerRepository.findCustomerByEmail(editCustomerRequest.getEmail());

        if (optionalCustomer.isEmpty())
            return false;

        Customer newCustomer = customerMapper.editCustomerRequestToCustomer(optionalCustomer.get(), editCustomerRequest);
        customerRepository.save(newCustomer);
        return true;
    }


    @Override
    public boolean editFavoriteProperties(EditFavoritePropertyRequest editFavoritePropertyRequest) {
        Optional<Customer> optionalCustomer = customerRepository.findByUserId(editFavoritePropertyRequest.getCustomerId());

        if (optionalCustomer.isEmpty())
            return false;

        Customer customer = optionalCustomer.get();

        if(editFavoritePropertyRequest.isFavorite()) {
            customer.getFavoriteProperties().add(editFavoritePropertyRequest.getPropertyId());
        } else {
            customer.getFavoriteProperties().remove(editFavoritePropertyRequest.getPropertyId());
        }

        customerRepository.save(customer);
        return true;
    }

}
