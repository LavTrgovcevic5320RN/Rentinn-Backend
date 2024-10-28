package rs.edu.raf.rentinn.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateSessionRequest {
    private String email;
    private Double totalPrice;
    private Long propertyId;
    private Long customerId;
    private String checkInDate;
    private String checkOutDate;
    private String successUrl;
    private String cancelUrl;
}
