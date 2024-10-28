package rs.edu.raf.rentinn.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingDto {

        private Long id;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private Double totalPrice;
        private String propertyName;

        private Long customerId;
        private Long propertyId;
        private ReviewDto review;
}
