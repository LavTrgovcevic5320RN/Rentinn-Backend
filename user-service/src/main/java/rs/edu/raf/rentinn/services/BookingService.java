package rs.edu.raf.rentinn.services;

import com.stripe.model.checkout.Session;
import rs.edu.raf.rentinn.dtos.BookingDto;
import rs.edu.raf.rentinn.dtos.PropertyDto;
import rs.edu.raf.rentinn.model.Booking;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.model.Property;
import rs.edu.raf.rentinn.requests.CreateBookingRequest;
import rs.edu.raf.rentinn.responses.DetailedResponse;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    List<PropertyDto> findAvailableProperties(String location, LocalDate checkInDate, LocalDate checkOutDate, int guests, int rooms);

    List<BookingDto> getBookingsByProperty(Long propertyId);

    List<BookingDto> getBookingsByUserId(Long userId);

    Booking createBooking(Booking booking);

    boolean isBookingAvailable(Long propertyId, LocalDate checkInDate, LocalDate checkOutDate);

    boolean isOwnerOfProperty(Customer customer, Property property);

    double calculateTotalPrice(Long propertyId, LocalDate checkInDate, LocalDate checkOutDate);

    void finalizeBookingFromSession(Session session);

    DetailedResponse canLeaveReview(Long propertyId, Long userId);
}
