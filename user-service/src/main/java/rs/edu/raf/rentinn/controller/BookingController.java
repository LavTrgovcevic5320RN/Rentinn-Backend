package rs.edu.raf.rentinn.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.rentinn.dtos.BookingDto;
import rs.edu.raf.rentinn.model.Booking;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.requests.CreateBookingRequest;
import rs.edu.raf.rentinn.requests.CreateSessionRequest;
import rs.edu.raf.rentinn.responses.DetailedResponse;
import rs.edu.raf.rentinn.services.BookingService;
import rs.edu.raf.rentinn.services.CustomerService;
import rs.edu.raf.rentinn.services.implementations.CustomerServiceImpl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final CustomerService customerService;

    @Autowired
    public BookingController(BookingService bookingService, CustomerService customerService) {
        this.bookingService = bookingService;
        this.customerService = customerService;
        Stripe.apiKey = "sk_test_51QDVkpGoH2c3s2KOHw1RnnrJEX1kU0yDrleviek8iXKlERvGITcCSmhB4pk5Suvbpqj7pqkMx6ONazYdDenFhcax00azyS5oOl";
    }


    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkAvailability(@RequestParam Long propertyId,
                                                     @RequestParam LocalDate checkInDate,
                                                     @RequestParam LocalDate checkOutDate) {
        boolean isAvailable = bookingService.isBookingAvailable(propertyId, checkInDate, checkOutDate);
        return ResponseEntity.ok(isAvailable);
    }


    @GetMapping("/booked-dates/{propertyId}")
    public List<BookingDto> getBookingsByProperty(@PathVariable Long propertyId) {
        return bookingService.getBookingsByProperty(propertyId);
    }


    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking newBooking = bookingService.createBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
    }


    @GetMapping("/calculateTotalPrice")
    public ResponseEntity<Double> calculateTotalPrice(@RequestParam Long propertyId, @RequestParam LocalDate checkInDate, @RequestParam LocalDate checkOutDate) {
        double totalPrice = bookingService.calculateTotalPrice(propertyId, checkInDate, checkOutDate);
        return ResponseEntity.ok(totalPrice);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<List<BookingDto>> getBookingsByUserId(@PathVariable Long userId) {
        List<BookingDto> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }


    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, "whsec_2175bf58ee88673cfee63a44c8ca88b34f35db9f72fe9c28d574ea263eb95e90");

            System.out.println("Event Type: " + event.getType());

            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getData().getObject();
                bookingService.finalizeBookingFromSession(session);
            }

            logger.debug("Unhandled event type: {}", event.getType());
            return ResponseEntity.ok("Unhandled event type");

        } catch (Exception e) {
            logger.error("Error processing webhook", e);
            return ResponseEntity.status(400).body("Error processing webhook: " + e.getMessage());
        }
    }


    @PostMapping(value = "/initialise-stripe", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get customer by jwt", description = "Returns customer by jwt",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody CreateSessionRequest request) {
        try {
            Long amount = request.getTotalPrice().longValue();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCustomerEmail(request.getEmail())
                    .setSuccessUrl("http://localhost:4200" +  request.getSuccessUrl())
//                    .setCancelUrl("http://localhost:4200/cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount(amount)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Hotel Booking")
                                                                    .build())
                                                    .build())
                                    .build())
                    .putMetadata("customerId", request.getCustomerId().toString())
                    .putMetadata("propertyId", request.getPropertyId().toString())
                    .putMetadata("checkInDate", request.getCheckInDate())
                    .putMetadata("checkOutDate", request.getCheckOutDate())
                    .build();

            logger.info("Creating session with params: {}", request.getCheckInDate());
            logger.info("Creating session with params: {}", request.getCheckOutDate());

            Session session = Session.create(params);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("id", session.getId());

            return ResponseEntity.ok(responseData);

        } catch (StripeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping(value = "/can-leave-review/{propertyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get customer by jwt", description = "Returns customer by jwt",
            parameters = {
                    @Parameter(name = "Authorization", description = "JWT token", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> canLeaveReview(@PathVariable Long propertyId) {
        Customer customer = this.customerService.findByJwt();

        DetailedResponse canLeaveReview = bookingService.canLeaveReview(propertyId, customer.getUserId());

        return ResponseEntity.ok(canLeaveReview);
    }

}

