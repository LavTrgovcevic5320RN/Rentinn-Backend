package rs.edu.raf.rentinn.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreationRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String gender;

    private Long dateOfBirth;

    private String nationality;

    private String phoneNumber;

}
