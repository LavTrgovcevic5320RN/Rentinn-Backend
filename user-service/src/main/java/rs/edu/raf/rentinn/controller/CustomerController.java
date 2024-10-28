package rs.edu.raf.rentinn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.rentinn.requests.EditCustomerRequest;
import rs.edu.raf.rentinn.requests.EditFavoritePropertyRequest;
import rs.edu.raf.rentinn.responses.CustomerResponse;
import rs.edu.raf.rentinn.services.CustomerService;

@RestController
@CrossOrigin
@RequestMapping("/customer")
@SecurityRequirement(name = "Authorization")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get customer by jwt", description = "Returns customer by jwt",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json"
//                            , schema = @Schema(implementation = CustomerResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CustomerResponse> readCustomerByJWT() {
        CustomerResponse customerResponse = this.customerService.findByJwt();

        if (customerResponse == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }


    @PutMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Admin edit customer", description = "Admin can edit a customer's info",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @PreAuthorize("hasAuthority('modifyCustomer')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer edited successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> editCustomer(@RequestBody EditCustomerRequest editCustomerRequest) {
        boolean edited = customerService.editCustomer(editCustomerRequest);
        return new ResponseEntity<>(edited, edited ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/favorites", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Admin edit customer", description = "Admin can edit a customer's info",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @PreAuthorize("hasAuthority('readProperty')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer edited successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> editFavoriteProperties(@RequestBody EditFavoritePropertyRequest editFavoritePropertyRequest) {
        boolean edited = customerService.editFavoriteProperties(editFavoritePropertyRequest);
        return new ResponseEntity<>(edited, edited ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
