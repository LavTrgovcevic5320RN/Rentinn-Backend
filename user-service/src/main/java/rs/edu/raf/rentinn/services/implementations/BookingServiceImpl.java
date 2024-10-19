package rs.edu.raf.rentinn.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.edu.raf.rentinn.dtos.PropertyDto;
import rs.edu.raf.rentinn.mapper.PropertyMapper;
import rs.edu.raf.rentinn.model.Booking;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.model.DailyPrice;
import rs.edu.raf.rentinn.model.Property;
import rs.edu.raf.rentinn.repositories.BookingRepository;
import rs.edu.raf.rentinn.repositories.DailyPriceRepository;
import rs.edu.raf.rentinn.repositories.PropertyRepository;
import rs.edu.raf.rentinn.services.BookingService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final DailyPriceRepository dailyPriceRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              DailyPriceRepository dailyPriceRepository,
                              PropertyRepository propertyRepository,
                              PropertyMapper propertyMapper) {
        this.bookingRepository = bookingRepository;
        this.dailyPriceRepository = dailyPriceRepository;
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
    }

    public List<PropertyDto> findAvailableProperties(String city, String country, LocalDate checkInDate, LocalDate checkOutDate, int guests, int rooms) {
        List<Property> properties = propertyRepository.findByLocation(city, country);

        return properties.stream()
                .filter(property -> isPropertyAvailable(property, checkInDate, checkOutDate))
                .toList()
                .stream()
                .map(propertyMapper::propertyToPropertyDto).toList();
    }

    private boolean isPropertyAvailable(Property property, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(property.getId(), checkInDate, checkOutDate);
        return overlappingBookings.isEmpty();
    }

    public boolean isOwnerOfProperty(Customer customer, Property property) {
        return customer.getProperties().contains(property);
    }

    public boolean isBookingAvailable(Long propertyId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(propertyId, checkInDate, checkOutDate);
        return overlappingBookings.isEmpty();
    }

    public double calculateTotalPrice(Long propertyId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<DailyPrice> prices = dailyPriceRepository.findPricesByDateRange(propertyId, checkInDate, checkOutDate);

        double totalPrice = 0;
        for (DailyPrice price : prices) {
            totalPrice += price.getPrice();
        }

        return totalPrice;
    }

    public Booking createBooking(Booking booking) {
        Customer customer = booking.getCustomer();
        Property property = booking.getProperty();

        if (isOwnerOfProperty(customer, property)) {
            throw new IllegalArgumentException("You cannot book your own property.");
        }

        if (!isBookingAvailable(property.getId(), booking.getCheckInDate(), booking.getCheckOutDate())) {
            throw new IllegalArgumentException("The property is already booked for the selected dates.");
        }

        double totalPrice = calculateTotalPrice(property.getId(), booking.getCheckInDate(), booking.getCheckOutDate());
        booking.setTotalPrice(totalPrice);

        return bookingRepository.save(booking);
    }
}
