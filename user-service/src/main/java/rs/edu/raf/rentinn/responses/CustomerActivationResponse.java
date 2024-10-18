package rs.edu.raf.rentinn.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerActivationResponse {

    private boolean isActivated;

    private String message;
}
