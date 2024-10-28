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
public class CreateBookingRequest {

    private Long userId;
    private Long propertyId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String sessionId;
}
