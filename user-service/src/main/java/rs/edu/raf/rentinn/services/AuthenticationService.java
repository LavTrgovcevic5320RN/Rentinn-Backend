package rs.edu.raf.rentinn.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.model.Employee;
import rs.edu.raf.rentinn.model.Permission;
import rs.edu.raf.rentinn.requests.CustomerActivationRequest;
import rs.edu.raf.rentinn.requests.CustomerCreationRequest;
import rs.edu.raf.rentinn.requests.LoginRequest;
import rs.edu.raf.rentinn.responses.CustomerRegistrationResponse;
import rs.edu.raf.rentinn.responses.LoginResponse;
import rs.edu.raf.rentinn.utils.Constants;
import rs.edu.raf.rentinn.utils.JwtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse generateLoginResponse(LoginRequest loginRequest, Object user) {
        List<String> permissions = new ArrayList<>();

        Set<Permission> permissionSet = extractPermissions(user);

        if (permissionSet != null && !permissionSet.isEmpty()) {
            permissions = permissionSet.stream().map(Permission::getName).collect(Collectors.toList());
        }

        return new LoginResponse(
                jwtUtil.generateToken(loginRequest.getEmail(), permissions),
                permissionSet.stream().map(Permission::getName).collect(Collectors.toList())
        );
    }

    public CustomerRegistrationResponse generateRegisterResponse(CustomerCreationRequest creationRequest) {

        return new CustomerRegistrationResponse(
                "Account created successfully and activation email sent to " + creationRequest.getEmail(),
                HttpStatus.OK,
                jwtUtil.generateToken(creationRequest.getEmail(), Constants.renterPermissions),
                Constants.renterPermissions
        );
    }

    public boolean activateAccount(CustomerActivationRequest activationRequest) {


        return true;
    }

    public LoginResponse generateLoginResponse(LoginRequest loginRequest) {
        return new LoginResponse(
                jwtUtil.generateToken(loginRequest.getEmail(), new ArrayList<>()),
                new ArrayList<>());
    }

    private String extractEmail(Object user){
        if(user instanceof Employee)
            return ((Employee) user).getEmail();

        if(user instanceof Customer)
            return ((Customer) user).getEmail();

        return null;
    }

    private Set<Permission> extractPermissions(Object user){
        if(user instanceof Employee)
            return ((Employee) user).getPermissions();

        if(user instanceof Customer)
            return ((Customer) user).getPermissions();

        return null;
    }
}

