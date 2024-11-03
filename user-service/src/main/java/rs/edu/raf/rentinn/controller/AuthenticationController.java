package rs.edu.raf.rentinn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.edu.raf.rentinn.exceptions.EmailAlreadyExistsException;
import rs.edu.raf.rentinn.exceptions.InvalidTokenException;
import rs.edu.raf.rentinn.mapper.CustomerMapper;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.model.Employee;
import rs.edu.raf.rentinn.repositories.CustomerRepository;
import rs.edu.raf.rentinn.repositories.EmployeeRepository;
import rs.edu.raf.rentinn.requests.CustomerActivationRequest;
import rs.edu.raf.rentinn.requests.CustomerCreationRequest;
import rs.edu.raf.rentinn.requests.LoginRequest;
import rs.edu.raf.rentinn.requests.RegistrationRequest;
import rs.edu.raf.rentinn.responses.CustomerActivationResponse;
import rs.edu.raf.rentinn.responses.CustomerRegistrationResponse;
import rs.edu.raf.rentinn.responses.LoginResponse;
import rs.edu.raf.rentinn.responses.RegistrationResponse;
import rs.edu.raf.rentinn.services.AuthenticationService;
import rs.edu.raf.rentinn.services.CustomerService;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public AuthenticationController(AuthenticationService authenticationService, AuthenticationManager authenticationManager, EmployeeRepository employeeRepository, CustomerRepository customerRepository, PasswordEncoder passwordEncoder, CustomerService customerService, CustomerMapper customerMapper) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @PostMapping("/login/employee")
    @Operation(summary = "User login", description = "Authenticate employee and return JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful login",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> employeeLogin(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            Optional<Employee> optionalEmployee = this.employeeRepository.findByEmail(loginRequest.getEmail());

            if (optionalEmployee.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            boolean matchesPassword = passwordEncoder.matches(loginRequest.getPassword(), optionalEmployee.get().getPassword());
            if(!matchesPassword){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: Passwords do not match");
            }

            LoginResponse loginResponse = this.authenticationService.generateLoginResponse(loginRequest, optionalEmployee.get());

            return ResponseEntity.ok(loginResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/login/customer")
    @Operation(summary = "User login", description = "Authenticate customer and return JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful login",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> customerLogin(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            Optional<Customer> optionalCustomer = this.customerRepository.findCustomerByEmail(loginRequest.getEmail());

            if (optionalCustomer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            boolean matchesPassword = passwordEncoder.matches(loginRequest.getPassword(), optionalCustomer.get().getPassword());
            if(!matchesPassword){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: Passwords do not match");
            }

            LoginResponse loginResponse = this.authenticationService.generateLoginResponse(loginRequest, optionalCustomer.get());

            return ResponseEntity.ok(loginResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }

/*    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerCreationRequest request) {
        try {
            customerService.registerCustomer(request);
            return ResponseEntity.ok("Customer created successfully. Check your email for the activation token.");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already taken.");
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateCustomer(@RequestBody CustomerActivationRequest activationRequest) {
        try {
            customerService.activateCustomer(activationRequest);
            return ResponseEntity.ok("Account activated successfully.");
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid activation token.");
        }
    }*/

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Create a new customer without an activation token")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> registerCustomer(@RequestBody @Valid RegistrationRequest registrationRequest) {
        try{
            if (customerRepository.findCustomerByEmail(registrationRequest.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use");
            }

            Customer customer = new Customer();
            customer.setFirstName(registrationRequest.getFirstName());
            customer.setLastName(registrationRequest.getLastName());
            customer.setEmail(registrationRequest.getEmail());
            customer.setPhoneNumber(registrationRequest.getPhoneNumber());
            customer.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            customer.setActive(true);

            customerRepository.save(customer);

            RegistrationResponse registrationResponse = this.authenticationService.generateRegisterResponse(registrationRequest, customer);

            return ResponseEntity.ok(registrationResponse);
        }  catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }


/*    @PostMapping("/register")
    @Operation(summary = "Customer wants to register", description = "Returns response if customer is successfully created" +
            "Also sends email to customer with activation link.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer activation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerRegistrationResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerCreationRequest customerCreationRequest) {
        boolean response = customerService.registerCustomer(customerCreationRequest);
        if (!response) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User with the same email already exists");
        }

        CustomerRegistrationResponse customerRegistrationResponse = this.authenticationService.generateRegisterResponse(customerCreationRequest);

        return ResponseEntity.ok(customerRegistrationResponse);
    }*/


    @PostMapping("/activateAccount")
    @Operation(summary = "Customer wants to activate his account", description = "Returns true if customer exists," +
            "false otherwise. Also sends email to customer with activation link.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer activation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerActivationResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> activateCustomerAccount(@RequestBody CustomerActivationRequest activationRequest) {
        boolean response = customerService.activateCustomer(activationRequest);
        if (!response) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no user with that activation token, please check that you have entered the token correctly");
        }

        return ResponseEntity.ok(new CustomerActivationResponse(true, "Account activated successfully"));
    }
}

