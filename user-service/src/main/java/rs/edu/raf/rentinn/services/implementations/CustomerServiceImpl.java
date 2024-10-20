package rs.edu.raf.rentinn.services.implementations;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.edu.raf.rentinn.mapper.CustomerMapper;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.repositories.CustomerRepository;
import rs.edu.raf.rentinn.requests.CustomerActivationRequest;
import rs.edu.raf.rentinn.requests.CustomerCreationRequest;
import rs.edu.raf.rentinn.responses.CustomerResponse;
import rs.edu.raf.rentinn.services.CustomerService;
import rs.edu.raf.rentinn.services.EmailService;
import rs.edu.raf.rentinn.services.EmployeeService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Value("${front.port}")
    private String frontPort;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final EmployeeService userService;
    private final CustomerMapper customerMapper;
    private final EmailService emailService;

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

    @Override
    public boolean registerCustomer(CustomerCreationRequest creationRequest) {
        Optional<Customer> optionalCustomer = this.customerRepository.findCustomerByEmail(creationRequest.getEmail());
        if (optionalCustomer.isPresent()) {
            return false;
//            return new CustomerRegistrationResponse("User with the same email already exists", HttpStatus.BAD_REQUEST);
        }

        Customer customer = customerMapper.createNewCustomer(creationRequest);
        customer = customerRepository.save(customer);

        String sendTo = customer.getEmail();
        String subject = "Account activation";
        String text = "Your activation code: " + customer.getActivationToken();
        emailService.sendEmail(sendTo, subject, text);
//        return new CustomerRegistrationResponse("Activation code sent to your email, please check your spam folder", HttpStatus.OK);
        return true;
    }

    @Override
    @Transactional
    public boolean activateCustomer(CustomerActivationRequest activationRequest) {
        String email = activationRequest.getEmail();
        String token = activationRequest.getActivationToken();
        logger.info("Email: " + email + " Token: " + token);

        logger.info("Activating customer with token: {}", token);

        Optional<Customer> optionalCustomer = customerRepository.findCustomerByEmailAndActivationToken(email, token);
        if (!optionalCustomer.isPresent()) {
            logger.warn("No customer found with token: {}", token);
            return false;
        }

        Customer customer = optionalCustomer.get();
        logger.info("Customer found: {}", customer);

        customer.setActive(true);
        customer.setActivationToken(null);

        customerRepository.save(customer);
        logger.info("Customer activation status updated: {}", customer);
        return true;
    }

    @Override
    public CustomerResponse findByJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null)
            return null;

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return findByEmail(userDetails.getUsername());
    }

    @Override
    public CustomerResponse findByEmail(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email).orElse(null);
        if(customer == null){
            return null;
        }
        CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(customer);

        return customerResponse;
    }
}
