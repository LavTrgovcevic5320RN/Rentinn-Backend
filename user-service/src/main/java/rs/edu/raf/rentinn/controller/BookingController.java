package rs.edu.raf.rentinn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.rentinn.model.Booking;
import rs.edu.raf.rentinn.services.BookingService;

import java.time.LocalDate;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkAvailability(@RequestParam Long propertyId,
                                                     @RequestParam LocalDate checkInDate,
                                                     @RequestParam LocalDate checkOutDate) {
        boolean isAvailable = bookingService.isBookingAvailable(propertyId, checkInDate, checkOutDate);
        return ResponseEntity.ok(isAvailable);
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking newBooking = bookingService.createBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
    }

    @GetMapping("/calculateTotalPrice")
    public ResponseEntity<Double> calculateTotalPrice(@RequestParam Long propertyId,
                                                      @RequestParam LocalDate checkInDate,
                                                      @RequestParam LocalDate checkOutDate) {
        double totalPrice = bookingService.calculateTotalPrice(propertyId, checkInDate, checkOutDate);
        return ResponseEntity.ok(totalPrice);
    }
}

