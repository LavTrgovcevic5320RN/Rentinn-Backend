package rs.edu.raf.rentinn.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
public class CustomerRegistrationResponse {

    private String message;

    private HttpStatus httpStatus;

    private String jwt;

    private List<String> permissions;

}
