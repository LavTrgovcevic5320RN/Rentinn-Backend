package rs.edu.raf.rentinn.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.repositories.CustomerRepository;
import rs.edu.raf.rentinn.services.CustomerService;
import rs.edu.raf.rentinn.services.EmployeeService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    @Value("${front.port}")
    private String frontPort;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final EmployeeService userService;
//    private final CustomerMapper customerMapper;
//    private final CurrencyService currencyService;
//    private final BankAccountService bankAccountService;
//    private final CompanyService companyService;
//    private final EmailService emailService;

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

}
