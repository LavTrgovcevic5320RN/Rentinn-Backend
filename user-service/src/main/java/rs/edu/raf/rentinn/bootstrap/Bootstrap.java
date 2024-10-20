package rs.edu.raf.rentinn.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.rentinn.model.*;
import rs.edu.raf.rentinn.repositories.*;
import rs.edu.raf.rentinn.utils.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Bootstrap implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final PropertyRepository propertyRepository;
    private final DailyPriceRepository dailyPriceRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    private final String imagesDir = "images/";
    private final String uploadsDir = "uploads/";

    @Autowired
    public Bootstrap(
            PermissionRepository permissionRepository,
            PasswordEncoder passwordEncoder,
            EmployeeRepository employeeRepository,
            CustomerRepository customerRepository,
            PropertyRepository propertyRepository,
            DailyPriceRepository dailyPriceRepository,
            BookingRepository bookingRepository, ReviewRepository reviewRepository) {
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.propertyRepository = propertyRepository;
        this.dailyPriceRepository = dailyPriceRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
    }

//    @Override
//    public void run(String... args) throws Exception {
//        seedPermissions();
//        System.out.println("Permissions seeded");
//
//        Customer customer1 = new Customer();
//        customer1.setEmail("customer@gmail.com");
//        customer1.setPassword(passwordEncoder.encode("Markovic01@"));
//        customer1.setFirstName("Marko");
//        customer1.setLastName("Markovic");
//        customer1.setJmbg("1234567890123");
//        customer1.setPhoneNumber("123456789");
//        customer1.setActive(true);
//        customer1.setDateOfBirth(LocalDate.of(1985, 5, 10).toEpochDay());
//        customer1.setGender("Male");
//        customer1.setNationality("USA");
//        customerRepository.save(customer1);
//
//        Customer customer2 = new Customer();
//        customer2.setEmail("admin@gmail.com");
//        customer2.setPassword(passwordEncoder.encode("Markovic01@"));
//        customer2.setFirstName("Jane");
//        customer2.setLastName("Smith");
//        customer2.setJmbg("9876543210987");
//        customer2.setPhoneNumber("987654321");
//        customer2.setActive(true);
//        customer2.setDateOfBirth(LocalDate.of(1990, 3, 22).toEpochDay());
//        customer2.setGender("Female");
//        customer2.setNationality("Canada");
//        customerRepository.save(customer2);
//
//        Customer customer3 = new Customer();
//        customer3.setEmail("renter@example.com");
//        customer3.setPassword(passwordEncoder.encode("Markovic01@"));
//        customer3.setFirstName("Alex");
//        customer3.setLastName("Johnson");
//        customer3.setJmbg("5647382910123");
//        customer3.setPhoneNumber("5647382910");
//        customer3.setActive(true);
//        customer3.setDateOfBirth(LocalDate.of(1995, 8, 15).toEpochDay());
//        customer3.setGender("Non-binary");
//        customer3.setNationality("UK");
//        customerRepository.save(customer3);
//
//        System.out.println("Customers seeded");
//
//
//
//        Property property1 = new Property();
//        property1.setTitle("Riviera Superior");
//        property1.setCountry("Greece");
//        property1.setCity("Crete");
//        property1.setAddress("Chania Old Town");
//        property1.setPostalCode("73132");
//        property1.setLatitude("35.5184506");
//        property1.setLongitude("24.0226517");
//
//        String folderName1 = "Riviera Superior Sweet Sea Front";
//        List<String> imagePaths1 = copyImagesToUploads(folderName1);
//        property1.setImagePaths(imagePaths1);
//
//        property1.setOwner(customer1);
//        propertyRepository.save(property1);
//
//        Property property2 = new Property();
//        property2.setTitle("Riviera Deluxe");
//        property2.setCountry("Greece");
//        property2.setCity("Crete");
//        property2.setAddress("Chania Old Town");
//        property2.setPostalCode("73132");
//        property2.setLatitude("35.5184506");
//        property2.setLongitude("24.0226517");
//
//        String folderName2 = "Riviera Deluxe Suite Sea Front";
//        List<String> imagePaths2 = copyImagesToUploads(folderName2);
//        property2.setImagePaths(imagePaths2);
//
//        property2.setOwner(customer1);
//        propertyRepository.save(property2);
//
//        Property property3 = new Property();
//        property3.setTitle("Sagara Candidasa Classic");
//        property3.setCountry("Indonesia");
//        property3.setCity("Bali");
//        property3.setAddress("Candidasa");
//        property3.setPostalCode("80851");
//        property3.setLatitude("-8.5100184");
//        property3.setLongitude("115.5704357");
//
//        String folderName3 = "Sagara Candidasa Classic Room";
//        List<String> imagePaths3 = copyImagesToUploads(folderName3);
//        property3.setImagePaths(imagePaths3);
//
//        property3.setOwner(customer2);
//        propertyRepository.save(property3);
//
//        System.out.println("Properties seeded");
//
//
//        generateDailyPricesForYear(property1);
//        generateDailyPricesForYear(property2);
//        generateDailyPricesForYear(property3);
//
//        System.out.println("Daily prices seeded");
//
//
//        LocalDate checkInDate = LocalDate.of(2024, 10, 18);
//        LocalDate checkOutDate = LocalDate.of(2024, 10, 20);
//
//        Booking booking1 = new Booking();
//        booking1.setCheckInDate(checkInDate);
//        booking1.setCheckOutDate(checkOutDate);
//        booking1.setTotalPrice(
//                dailyPriceRepository
//                        .findPricesByDateRange(property1.getId(), checkInDate, checkOutDate)
//                        .stream()
//                        .mapToDouble(DailyPrice::getPrice)
//                        .sum());
//        booking1.setCustomer(customer3);
//        booking1.setProperty(property1);
//        bookingRepository.save(booking1);
//
//        Booking booking2 = new Booking();
//        booking2.setCheckInDate(LocalDate.of(2024, 11, 5));
//        booking2.setCheckOutDate(LocalDate.of(2024, 11, 10));
//        booking2.setTotalPrice(120.0);
//        booking2.setCustomer(customer3);
//        booking2.setProperty(property3);
//        bookingRepository.save(booking2);
//
//        System.out.println("Bookings seeded");
//
//
//
//        Review review1 = new Review();
//        review1.setComment("Amazing stay at the Riviera Superior!");
//        review1.setRating(5.0);
//        review1.setCustomer(customer3);
//        review1.setProperty(property1);
//        review1.setBooking(booking1);
//        reviewRepository.save(review1);
//
//        Review review2 = new Review();
//        review2.setComment("Very cozy place, but could improve the internet speed.");
//        review2.setRating(4.2);
//        review2.setCustomer(customer3);
//        review2.setProperty(property3);
//        review2.setBooking(booking2);
//        reviewRepository.save(review2);
//
//        System.out.println("Reviews seeded");
//    }


//    @Override
//    public void run(String... args) throws Exception {
//        seedPermissions();
//        System.out.println("Permissions seeded");
//
//        // Create Customers
//        Customer customer1 = createCustomer("customer@gmail.com", "Markovic01@", "Marko", "Markovic", "1234567890123", "123456789", LocalDate.of(1985, 5, 10), "Male", "USA");
//        Customer customer2 = createCustomer("admin@gmail.com", "Markovic01@", "Jane", "Smith", "9876543210987", "987654321", LocalDate.of(1990, 3, 22), "Female", "Canada");
//        Customer customer3 = createCustomer("renter@example.com", "Markovic01@", "Alex", "Johnson", "5647382910123", "5647382910", LocalDate.of(1995, 8, 15), "Non-binary", "UK");
//
//        System.out.println("Customers seeded");
//
//        // Create Properties
//        Property property1 = createProperty("Riviera Superior", "Greece", "Crete", "Chania Old Town", "73132", "35.5184506", "24.0226517", "Riviera Superior Sweet Sea Front", customer1);
//        Property property2 = createProperty("Riviera Deluxe", "Greece", "Crete", "Chania Old Town", "73132", "35.5184506", "24.0226517", "Riviera Deluxe Suite Sea Front", customer1);
//        Property property3 = createProperty("Sagara Candidasa Classic", "Indonesia", "Bali", "Candidasa", "80851", "-8.5100184", "115.5704357", "Sagara Candidasa Classic Room", customer2);
//        Property property4 = createProperty("Cosmopolitan Apartment", "Indonesia", "Jakarta", "Jl. Sudirman", "10220", "-6.200000", "106.816666", "Cosmopolitan Apartment", customer3);
//        Property property5 = createProperty("Genggong Villa", "Indonesia", "Bali", "Padang Bai", "80871", "-8.533333", "115.516667", "Genggong", customer3);
//        Property property6 = createProperty("Riviera Classic Suite Sea View", "Greece", "Crete", "Heraklion", "71409", "35.339841", "25.144213", "Riviera Classic Suite Sea View", customer1);
//        Property property7 = createProperty("Riviera Executive Suite Sea View", "Greece", "Crete", "Rethymno", "74100", "35.366", "24.483", "Riviera Executive Suite Sea View", customer2);
//        Property property8 = createProperty("Sagara Candidasa Premiere", "Indonesia", "Bali", "Candidasa", "80851", "-8.5100184", "115.5704357", "Sagara Candidasa Premiere Room", customer2);
//
//        System.out.println("Properties seeded");
//
//        // Generate daily prices for each property
//        generateDailyPricesForYear(property1);
//        generateDailyPricesForYear(property2);
//        generateDailyPricesForYear(property3);
//        generateDailyPricesForYear(property4);
//        generateDailyPricesForYear(property5);
//        generateDailyPricesForYear(property6);
//        generateDailyPricesForYear(property7);
//        generateDailyPricesForYear(property8);
//
//        System.out.println("Daily prices seeded");
//
//        // Create Bookings
//        Booking booking1 = createBooking(LocalDate.of(2024, 10, 18), LocalDate.of(2024, 10, 20), customer3, property1);
//        Booking booking2 = createBooking(LocalDate.of(2024, 11, 5), LocalDate.of(2024, 11, 10), customer3, property3);
//        Booking booking3 = createBooking(LocalDate.of(2024, 12, 15), LocalDate.of(2024, 12, 20), customer2, property4);
//        Booking booking4 = createBooking(LocalDate.of(2025, 1, 5), LocalDate.of(2025, 1, 10), customer1, property5);
//        Booking booking5 = createBooking(LocalDate.of(2024, 11, 12), LocalDate.of(2024, 11, 18), customer2, property6);
//        Booking booking6 = createBooking(LocalDate.of(2024, 11, 20), LocalDate.of(2024, 11, 25), customer1, property7);
//        Booking booking7 = createBooking(LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 7), customer3, property8);
//
//        System.out.println("Bookings seeded");
//
//        // Create Reviews
//        createReview("Amazing stay at the Riviera Superior!", 5.0, customer3, property1, booking1);
//        createReview("Very cozy place, but could improve the internet speed.", 4.2, customer3, property3, booking2);
//        createReview("Loved the view from Cosmopolitan Apartment. Great amenities!", 4.8, customer2, property4, booking3);
//        createReview("Quiet and peaceful place, perfect for a retreat!", 4.7, customer1, property5, booking4);
//        createReview("The sea view was breathtaking, but service could improve.", 4.0, customer2, property6, booking5);
//        createReview("Luxurious experience with top-notch facilities!", 5.0, customer1, property7, booking6);
//        createReview("Premiere room was stunning, highly recommend!", 4.9, customer3, property8, booking7);
//
//        System.out.println("Reviews seeded");
//    }
//
//    private Customer createCustomer(String email, String password, String firstName, String lastName, String jmbg, String phoneNumber, LocalDate dateOfBirth, String gender, String nationality) {
//        Customer customer = new Customer();
//        customer.setEmail(email);
//        customer.setPassword(passwordEncoder.encode(password));
//        customer.setFirstName(firstName);
//        customer.setLastName(lastName);
//        customer.setJmbg(jmbg);
//        customer.setPhoneNumber(phoneNumber);
//        customer.setActive(true);
//        customer.setDateOfBirth(dateOfBirth.toEpochDay());
//        customer.setGender(gender);
//        customer.setNationality(nationality);
//        customerRepository.save(customer);
//        return customer;
//    }
//
//    private Property createProperty(String title, String country, String city, String address, String postalCode, String latitude, String longitude, String folderName, Customer owner) throws IOException {
//        Property property = new Property();
//        property.setTitle(title);
//        property.setCountry(country);
//        property.setCity(city);
//        property.setAddress(address);
//        property.setPostalCode(postalCode);
//        property.setLatitude(latitude);
//        property.setLongitude(longitude);
//
//        List<String> imagePaths = copyImagesToUploads(folderName);
//        property.setImagePaths(imagePaths);
//
//        property.setOwner(owner);
//        propertyRepository.save(property);
//        return property;
//    }
//
//    private Booking createBooking(LocalDate checkInDate, LocalDate checkOutDate, Customer customer, Property property) {
//        Booking booking = new Booking();
//        booking.setCheckInDate(checkInDate);
//        booking.setCheckOutDate(checkOutDate);
//        booking.setTotalPrice(
//                dailyPriceRepository
//                        .findPricesByDateRange(property.getId(), checkInDate, checkOutDate)
//                        .stream()
//                        .mapToDouble(DailyPrice::getPrice)
//                        .sum());
//        booking.setCustomer(customer);
//        booking.setProperty(property);
//        bookingRepository.save(booking);
//        return booking;
//    }
//
//    private Review createReview(String comment, double rating, Customer customer, Property property, Booking booking) {
//        Review review = new Review();
//        review.setComment(comment);
//        review.setRating(rating);
//        review.setCustomer(customer);
//        review.setProperty(property);
//        review.setBooking(booking);
//        reviewRepository.save(review);
//        return review;
//    }


    @Override
    public void run(String... args) throws Exception {
        seedPermissions();
        System.out.println("Permissions seeded");

        // Create Customers
        Customer customer1 = createCustomer("customer@gmail.com", "Markovic01@", "Marko", "Markovic", "1234567890123", "123456789", LocalDate.of(1985, 5, 10), "Male", "USA");
        Customer customer2 = createCustomer("admin@gmail.com", "Markovic01@", "Jane", "Smith", "9876543210987", "987654321", LocalDate.of(1990, 3, 22), "Female", "Canada");
        Customer customer3 = createCustomer("renter@example.com", "Markovic01@", "Alex", "Johnson", "5647382910123", "5647382910", LocalDate.of(1995, 8, 15), "Non-binary", "UK");

        System.out.println("Customers seeded");

        // Create Properties
        Property property1 = createProperty("Riviera Superior", "Greece", "Crete", "Chania Old Town", "73132", "35.5184506", "24.0226517", "Riviera Superior Sweet Sea Front", customer1);
        Property property2 = createProperty("Riviera Deluxe", "Greece", "Crete", "Chania Old Town", "73132", "35.5184506", "24.0226517", "Riviera Deluxe Suite Sea Front", customer1);
        Property property3 = createProperty("Sagara Candidasa Classic", "Indonesia", "Bali", "Candidasa", "80851", "-8.5100184", "115.5704357", "Sagara Candidasa Classic Room", customer2);
        Property property4 = createProperty("Cosmopolitan Apartment", "Indonesia", "Jakarta", "Jl. Sudirman", "10220", "-6.200000", "106.816666", "Cosmopolitan Apartment", customer3);
        Property property5 = createProperty("Genggong Villa", "Indonesia", "Bali", "Padang Bai", "80871", "-8.533333", "115.516667", "Genggong", customer3);
        Property property6 = createProperty("Riviera Classic Suite Sea View", "Greece", "Crete", "Heraklion", "71409", "35.339841", "25.144213", "Riviera Classic Suite Sea View", customer1);
        Property property7 = createProperty("Riviera Executive Suite Sea View", "Greece", "Crete", "Rethymno", "74100", "35.366", "24.483", "Riviera Executive Suite Sea View", customer2);
        Property property8 = createProperty("Sagara Candidasa Premiere", "Indonesia", "Bali", "Candidasa", "80851", "-8.5100184", "115.5704357", "Sagara Candidasa Premiere Room", customer2);

        System.out.println("Properties seeded");

        // Generate daily prices for each property
        generateDailyPricesForYear(property1);
        generateDailyPricesForYear(property2);
        generateDailyPricesForYear(property3);
        generateDailyPricesForYear(property4);
        generateDailyPricesForYear(property5);
        generateDailyPricesForYear(property6);
        generateDailyPricesForYear(property7);
        generateDailyPricesForYear(property8);

        System.out.println("Daily prices seeded");

        // Create 21 Bookings (3x as many)
        Booking booking1 = createBooking(LocalDate.of(2024, 10, 18), LocalDate.of(2024, 10, 20), customer3, property1);
        Booking booking2 = createBooking(LocalDate.of(2024, 11, 5), LocalDate.of(2024, 11, 10), customer3, property3);
        Booking booking3 = createBooking(LocalDate.of(2024, 12, 15), LocalDate.of(2024, 12, 20), customer2, property4);
        Booking booking4 = createBooking(LocalDate.of(2025, 1, 5), LocalDate.of(2025, 1, 10), customer1, property5);
        Booking booking5 = createBooking(LocalDate.of(2024, 11, 12), LocalDate.of(2024, 11, 18), customer2, property6);
        Booking booking6 = createBooking(LocalDate.of(2024, 11, 20), LocalDate.of(2024, 11, 25), customer1, property7);
        Booking booking7 = createBooking(LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 7), customer3, property8);
        Booking booking8 = createBooking(LocalDate.of(2024, 12, 22), LocalDate.of(2024, 12, 28), customer2, property1);
        Booking booking9 = createBooking(LocalDate.of(2025, 1, 15), LocalDate.of(2025, 1, 18), customer1, property2);
        Booking booking10 = createBooking(LocalDate.of(2025, 2, 10), LocalDate.of(2025, 2, 15), customer3, property3);
        Booking booking11 = createBooking(LocalDate.of(2024, 12, 28), LocalDate.of(2025, 1, 2), customer2, property4);
        Booking booking12 = createBooking(LocalDate.of(2025, 2, 5), LocalDate.of(2025, 2, 10), customer3, property5);
        Booking booking13 = createBooking(LocalDate.of(2024, 11, 15), LocalDate.of(2024, 11, 20), customer2, property6);
        Booking booking14 = createBooking(LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 7), customer1, property7);
        Booking booking15 = createBooking(LocalDate.of(2025, 4, 10), LocalDate.of(2025, 4, 12), customer3, property8);
        Booking booking16 = createBooking(LocalDate.of(2025, 3, 15), LocalDate.of(2025, 3, 20), customer1, property2);
        Booking booking17 = createBooking(LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 5), customer2, property1);
        Booking booking18 = createBooking(LocalDate.of(2024, 12, 10), LocalDate.of(2024, 12, 15), customer3, property4);
        Booking booking19 = createBooking(LocalDate.of(2025, 5, 5), LocalDate.of(2025, 5, 12), customer2, property7);
        Booking booking20 = createBooking(LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 7), customer1, property6);
        Booking booking21 = createBooking(LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 5), customer3, property5);

        System.out.println("Bookings seeded");

        // Create 21 Reviews (3x as many)
        createReview("Amazing stay at the Riviera Superior!", 5.0, customer3, property1, booking1);
        createReview("Very cozy place, but could improve the internet speed.", 4.2, customer3, property3, booking2);
        createReview("Loved the view from Cosmopolitan Apartment. Great amenities!", 4.8, customer2, property4, booking3);
        createReview("Quiet and peaceful place, perfect for a retreat!", 4.7, customer1, property5, booking4);
        createReview("The sea view was breathtaking, but service could improve.", 4.0, customer2, property6, booking5);
        createReview("Luxurious experience with top-notch facilities!", 5.0, customer1, property7, booking6);
        createReview("Premiere room was stunning, highly recommend!", 4.9, customer3, property8, booking7);
        createReview("Great experience, friendly staff!", 4.5, customer2, property1, booking8);
        createReview("Nice and clean, but could be a bit closer to the city center.", 4.3, customer1, property2, booking9);
        createReview("Lovely time at Sagara Candidasa!", 4.6, customer3, property3, booking10);
        createReview("The view from Cosmopolitan was breathtaking!", 4.9, customer2, property4, booking11);
        createReview("Great location, close to the beach.", 4.4, customer3, property5, booking12);
        createReview("Fantastic sea view, very relaxing.", 4.7, customer2, property6, booking13);
        createReview("Executive suite was superb! Highly recommend!", 5.0, customer1, property7, booking14);
        createReview("Premiere room had everything we needed for a perfect stay.", 4.9, customer3, property8, booking15);
        createReview("The deluxe suite was beautiful, but a little noisy at night.", 4.1, customer1, property2, booking16);
        createReview("Superior room was great, very quiet and peaceful.", 4.8, customer2, property1, booking17);
        createReview("Cosmopolitan apartment exceeded our expectations!", 4.9, customer3, property4, booking18);
        createReview("Executive suite was very comfortable, great service!", 5.0, customer2, property7, booking19);
        createReview("Classic suite sea view was just incredible!", 4.9, customer1, property6, booking20);
        createReview("The villa was beautiful and private, perfect for a romantic getaway.", 4.8, customer3, property5, booking21);

        System.out.println("Reviews seeded");
    }

    private Customer createCustomer(String email, String password, String firstName, String lastName, String jmbg, String phoneNumber, LocalDate dateOfBirth, String gender, String nationality) {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setJmbg(jmbg);
        customer.setPhoneNumber(phoneNumber);
        customer.setActive(true);
        customer.setDateOfBirth(dateOfBirth.toEpochDay());
        customer.setGender(gender);
        customer.setNationality(nationality);
        customerRepository.save(customer);
        return customer;
    }

    private Property createProperty(String title, String country, String city, String address, String postalCode, String latitude, String longitude, String folderName, Customer owner) throws IOException {
        Property property = new Property();
        property.setTitle(title);
        property.setCountry(country);
        property.setCity(city);
        property.setAddress(address);
        property.setPostalCode(postalCode);
        property.setLatitude(latitude);
        property.setLongitude(longitude);
        property.setCheckIn("14:00");
        property.setCheckOut("10:00");

        List<String> imagePaths = copyImagesToUploads(folderName);
        property.setImagePaths(imagePaths);

        property.setOwner(owner);
        propertyRepository.save(property);
        return property;
    }

    private Booking createBooking(LocalDate checkInDate, LocalDate checkOutDate, Customer customer, Property property) {
        Booking booking = new Booking();
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setTotalPrice(
                dailyPriceRepository
                        .findPricesByDateRange(property.getId(), checkInDate, checkOutDate)
                        .stream()
                        .mapToDouble(DailyPrice::getPrice)
                        .sum());
        booking.setCustomer(customer);
        booking.setProperty(property);
        bookingRepository.save(booking);
        return booking;
    }

    private Review createReview(String comment, double rating, Customer customer, Property property, Booking booking) {
        Review review = new Review();
        review.setComment(comment);
        review.setRating(rating);
        review.setCustomer(customer);
        review.setProperty(property);
        review.setBooking(booking);
        reviewRepository.save(review);
        return review;
    }



    private void generateDailyPricesForYear(Property property) {
        Random random = new Random();
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            double randomPrice = 30 + (500 - 30) * random.nextDouble();

            int consecutiveDays = random.nextInt(5) + 1;

            for (int i = 0; i < consecutiveDays && !currentDate.isAfter(endDate); i++) {
                DailyPrice dailyPrice = new DailyPrice();
                dailyPrice.setDate(currentDate);
                dailyPrice.setPrice(randomPrice);
                dailyPrice.setProperty(property);
                dailyPriceRepository.save(dailyPrice);  // Sačuvamo cenu u bazi

                currentDate = currentDate.plusDays(1); // Pomeramo datum na sledeći dan
            }
        }
    }

    private List<String> copyImagesToUploads(String propertyFolderName) throws IOException {
        List<String> uploadedImagePaths = new ArrayList<>();

        // Create the path for the property folder in 'images' directory
        Path propertyImagesPath = Paths.get(imagesDir + propertyFolderName);

        // Create a directory for this property in uploads
        Path propertyUploadsPath = Paths.get(uploadsDir + propertyFolderName);
        if (!Files.exists(propertyUploadsPath)) {
            Files.createDirectories(propertyUploadsPath);
        }

        // Get all image files from the property directory
        try (Stream<Path> stream = Files.list(propertyImagesPath)) {
            uploadedImagePaths = stream
                    .filter(Files::isRegularFile) // Filter only files (ignore subfolders)
                    .map(imageFile -> {
                        try {
                            // Generate a unique file name with UUID
                            String newFileName = UUID.randomUUID().toString() + "-" + imageFile.getFileName().toString();
                            Path targetPath = propertyUploadsPath.resolve(newFileName);

                            // Copy the image to the target folder
                            Files.copy(imageFile, targetPath, StandardCopyOption.REPLACE_EXISTING);

                            // Add the relative path to the list (for storage in the database)
                            return uploadsDir + propertyFolderName + "/" + newFileName;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(path -> path != null) // Filter out nulls (if any errors occurred)
                    .collect(Collectors.toList());
        }

        return uploadedImagePaths;
    }

    private void seedPermissions() {
        for(String s : Constants.allPermissions) {
            if(permissionRepository.findByName(s).isPresent()) {
                continue;
            }

            Permission permission = new Permission();
            permission.setName(s);
            permission.setDescription(s);
            permissionRepository.save(permission);
        }
    }

    private Employee generateEmployee(final String email, final String password, final String firstName, final String lastName, final String position, final Boolean active){
        Employee employee = new Employee();
        employee.setEmail(email);
        employee.setPassword(passwordEncoder.encode(password));
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setActive(active);
        employee.setPermissions(new HashSet<>(permissionRepository.findAll()));
        if (employeeRepository.findByEmail(employee.getEmail()).isEmpty()) {
            employeeRepository.save(employee);
        }else {
            employee = employeeRepository.findByEmail(employee.getEmail()).get();
        }
        return employee;
    }

    private Customer generateCustomer(final String email, final String password, final String firstName, final String lastName, final String position, final Boolean active){
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setActive(active);
        customer.setPermissions(new HashSet<>(permissionRepository.findAll()));
        if (customerRepository.findByEmail(customer.getEmail()).isEmpty()) {
            customerRepository.save(customer);
        }else {
            customer = customerRepository.findByEmail(customer.getEmail()).get();
        }
        return customer;
    }
}
