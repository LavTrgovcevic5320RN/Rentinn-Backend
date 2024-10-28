package rs.edu.raf.rentinn.services;

import com.stripe.model.checkout.Session;
import rs.edu.raf.rentinn.dtos.BookingDto;
import rs.edu.raf.rentinn.dtos.PropertyDto;
import rs.edu.raf.rentinn.model.Booking;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.model.Property;
import rs.edu.raf.rentinn.requests.CreateBookingRequest;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    List<PropertyDto> findAvailableProperties(String location, LocalDate checkInDate, LocalDate checkOutDate, int guests, int rooms);

    boolean isOwnerOfProperty(Customer customer, Property property);

    boolean isBookingAvailable(Long propertyId, LocalDate checkInDate, LocalDate checkOutDate);

    List<BookingDto> getBookingsByProperty(Long propertyId);

    double calculateTotalPrice(Long propertyId, LocalDate checkInDate, LocalDate checkOutDate);

    Booking createBooking(Booking booking);

    List<BookingDto> getBookingsByUserId(Long userId);

    void confirmBooking(CreateBookingRequest createBookingRequest);

    void finalizeBookingFromSession(Session session);
}
