package rs.edu.raf.rentinn.services;

import rs.edu.raf.rentinn.dtos.PropertyDto;
import rs.edu.raf.rentinn.model.Booking;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.model.Property;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    List<PropertyDto> findAvailableProperties(String city, String country, LocalDate checkInDate, LocalDate checkOutDate, int guests, int rooms);

    boolean isOwnerOfProperty(Customer customer, Property property);

    boolean isBookingAvailable(Long propertyId, LocalDate checkInDate, LocalDate checkOutDate);

    double calculateTotalPrice(Long propertyId, LocalDate checkInDate, LocalDate checkOutDate);

    Booking createBooking(Booking booking);


}
