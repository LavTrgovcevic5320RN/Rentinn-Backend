package rs.edu.raf.rentinn.services.implementations;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.edu.raf.rentinn.dtos.BookingDto;
import rs.edu.raf.rentinn.dtos.PropertyDto;
import rs.edu.raf.rentinn.mapper.BookingMapper;
import rs.edu.raf.rentinn.mapper.PropertyMapper;
import rs.edu.raf.rentinn.model.Booking;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.model.DailyPrice;
import rs.edu.raf.rentinn.model.Property;
import rs.edu.raf.rentinn.repositories.BookingRepository;
import rs.edu.raf.rentinn.repositories.CustomerRepository;
import rs.edu.raf.rentinn.repositories.DailyPriceRepository;
import rs.edu.raf.rentinn.repositories.PropertyRepository;
import rs.edu.raf.rentinn.requests.CreateBookingRequest;
import rs.edu.raf.rentinn.responses.DetailedResponse;
import rs.edu.raf.rentinn.services.BookingService;
import rs.edu.raf.rentinn.services.CustomerService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final DailyPriceRepository dailyPriceRepository;
    private final PropertyRepository propertyRepository;
    private final CustomerRepository customerRepository;
    private final PropertyMapper propertyMapper;
    private final BookingMapper bookingMapper;
    private final CustomerService customerService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              DailyPriceRepository dailyPriceRepository,
                              PropertyRepository propertyRepository,
                              CustomerRepository customerRepository,
                              PropertyMapper propertyMapper,
                              BookingMapper bookingMapper,
                              CustomerService customerService) {
        this.bookingRepository = bookingRepository;
        this.dailyPriceRepository = dailyPriceRepository;
        this.propertyRepository = propertyRepository;
        this.customerRepository = customerRepository;
        this.propertyMapper = propertyMapper;
        this.bookingMapper = bookingMapper;
        this.customerService = customerService;
    }

    public List<PropertyDto> findAvailableProperties(String location, LocalDate checkInDate, LocalDate checkOutDate, int guests, int rooms) {
        String[] locationParts = location.split(",");
        String part1 = locationParts[0].trim();
        Optional<String> part2 = locationParts.length > 1 ? Optional.of(locationParts[1].trim()) : Optional.empty();

        List<Property> properties;

        if (part2.isPresent()) {
            properties = propertyRepository.findByCityAndCountryOrCountryAndCity(part1, part2.get(), part1, part2.get());
        } else {
            properties = propertyRepository.findByCityOrCountry(part1);
        }

        return properties.stream()
                .filter(property -> isPropertyAvailable(property, checkInDate, checkOutDate))
                .toList()
                .stream()
                .map(propertyMapper::propertyToPropertyDto).toList();
    }


    public List<BookingDto> getBookingsByProperty(Long propertyId) {
        return bookingRepository.findByPropertyId(propertyId).stream().map(bookingMapper::bookingToBookingDto).toList();
    }


    public List<BookingDto> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId).stream().map(bookingMapper::bookingToBookingDto).toList();
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


    private boolean isPropertyAvailable(Property property, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(property.getId(), checkInDate, checkOutDate);
        return overlappingBookings.isEmpty();
    }


    public boolean isBookingAvailable(Long propertyId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(propertyId, checkInDate, checkOutDate);
        return overlappingBookings.isEmpty();
    }


    public boolean isOwnerOfProperty(Customer customer, Property property) {
        return customer.getProperties().contains(property);
    }


    public double calculateTotalPrice(Long propertyId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<DailyPrice> prices = dailyPriceRepository.findPricesByDateRange(propertyId, checkInDate, checkOutDate);

        double totalPrice = 0;
        for (DailyPrice price : prices) {
            totalPrice += price.getPrice();
        }

        return totalPrice;
    }


    public void finalizeBookingFromSession(Session session) {
        try {
            Long amount = session.getAmountTotal();

            String customerId = session.getMetadata().get("customerId");
            String propertyId = session.getMetadata().get("propertyId");
            LocalDate checkInDate = LocalDate.parse(session.getMetadata().get("checkInDate"));
            LocalDate checkOutDate = LocalDate.parse(session.getMetadata().get("checkOutDate"));

            Customer customer = customerRepository.findById(Long.parseLong(customerId))
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            Property property = propertyRepository.findById(Long.parseLong(propertyId))
                    .orElseThrow(() -> new RuntimeException("Property not found"));

            if (isOwnerOfProperty(customer, property)) {
                throw new IllegalArgumentException("You cannot book your own property.");
            }

            if (!isBookingAvailable(property.getId(), checkInDate, checkOutDate)) {
                throw new IllegalArgumentException("The property is already booked for the selected dates.");
            }

            Booking booking = new Booking();
            booking.setCustomer(customer);
            booking.setProperty(property);
            booking.setCheckInDate(checkInDate);
            booking.setCheckOutDate(checkOutDate);
            booking.setTotalPrice(Double.valueOf(amount));
            bookingRepository.save(booking);

        } catch (Exception e) {
            throw new RuntimeException("Error finalizing booking: " + e.getMessage());
        }
    }


    @Override
    public DetailedResponse canLeaveReview(Long propertyId, Long userId) {
        List<Booking> bookings = bookingRepository.findBookingsByPropertyIdAndUserId(propertyId, userId);

        if (bookings.isEmpty()) {
            return new DetailedResponse(false, "You don't have any previous bookings on this property.");
        }

        for (Booking booking : bookings) {
            if (booking.getCheckOutDate().isBefore(LocalDate.now()) && booking.getReview() == null) {
                return new DetailedResponse(true, "You can leave a review.");
            }
        }

        return new DetailedResponse(false, "You can only leave a review after your stay is completed.");
    }


}
