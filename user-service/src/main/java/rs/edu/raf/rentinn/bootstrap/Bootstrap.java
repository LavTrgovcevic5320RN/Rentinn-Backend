package rs.edu.raf.rentinn.bootstrap;

import jakarta.transaction.Transactional;
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

    @Override
    public void run(String... args) throws Exception {
        seedPermissions();
        System.out.println("Permissions seeded");

        Customer customer1 = createCustomer("customer@gmail.com", "Markovic01@", "Marko", "Markovic", "1234567890123", "123456789", LocalDate.of(1985, 5, 10), "Male", "USA", "New York, 123");
        Customer customer2 = createCustomer("admin@gmail.com", "Markovic01@", "Jane", "Smith", "9876543210987", "987654321", LocalDate.of(1990, 3, 22), "Female", "Canada", "Kentacky, 123");
        Customer customer3 = createCustomer("renter@example.com", "Markovic01@", "Alex", "Johnson", "5647382910123", "5647382910", LocalDate.of(1995, 8, 15), "Non-binary", "UK", "London, 456");
        Customer customer4 = createCustomer("marko@gmail.com", "Markovic01@", "Marko", "Markovic", "1234567890136", "123456789", LocalDate.of(1985, 5, 10), "Male", "USA", "New York, 123");

        System.out.println("Customers seeded");

        Property property1 = createProperty("Riviera Superior", "Greece", "Crete", "Chania Old Town", "73132", "35.5184506", "24.0226517", "Riviera Superior Sweet Sea Front", customer1);
        Property property2 = createProperty("Riviera Deluxe", "Greece", "Crete", "Chania Old Town", "73132", "35.5184506", "24.0226517", "Riviera Deluxe Suite Sea Front", customer1);
        Property property3 = createProperty("Sagara Candidasa Classic", "Indonesia", "Bali", "Candidasa", "80851", "-8.5100184", "115.5704357", "Sagara Candidasa Classic Room", customer2);
        Property property4 = createProperty("Cosmopolitan Apartment", "Serbia", "Belgrade", "Skadarska", "11000", "-6.200000", "106.816666", "Cosmopolitan Apartment", customer3);
        Property property5 = createProperty("Genggong Villa", "Indonesia", "Bali", "Padang Bai", "80871", "-8.533333", "115.516667", "Genggong", customer3);
        Property property6 = createProperty("Riviera Classic Suite Sea View", "Greece", "Crete", "Heraklion", "71409", "35.339841", "25.144213", "Riviera Classic Suite Sea View", customer1);
        Property property7 = createProperty("Riviera Executive Suite Sea View", "Greece", "Crete", "Rethymno", "74100", "35.366", "24.483", "Riviera Executive Suite Sea View", customer2);
        Property property8 = createProperty("Sagara Candidasa Premiere", "Indonesia", "Bali", "Candidasa", "80851", "-8.5100184", "115.5704357", "Sagara Candidasa Premiere Room", customer2);

        System.out.println("Properties seeded");

        generateDailyPricesForYear(property1, 2024);
        generateDailyPricesForYear(property1, 2025);
        generateDailyPricesForYear(property2, 2024);
        generateDailyPricesForYear(property2, 2025);
        generateDailyPricesForYear(property3, 2024);
        generateDailyPricesForYear(property3, 2025);
        generateDailyPricesForYear(property4, 2024);
        generateDailyPricesForYear(property4, 2025);
        generateDailyPricesForYear(property5, 2024);
        generateDailyPricesForYear(property5, 2025);
        generateDailyPricesForYear(property6, 2024);
        generateDailyPricesForYear(property6, 2025);
        generateDailyPricesForYear(property7, 2024);
        generateDailyPricesForYear(property7, 2025);
        generateDailyPricesForYear(property8, 2024);
        generateDailyPricesForYear(property8, 2025);

        System.out.println("Daily prices seeded");

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

//        createReview("Amazing stay at the Riviera Superior!", 5.0, customer3, property1, booking1);
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

    private Customer createCustomer(String email, String password,
                                    String firstName, String lastName,
                                    String jmbg, String phoneNumber,
                                    LocalDate dateOfBirth, String gender,
                                    String nationality, String address) {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setJmbg(jmbg);
        customer.setPhoneNumber(phoneNumber);
        customer.setActive(true);
        customer.setDateOfBirth(dateOfBirth);
        customer.setGender(gender);
        customer.setNationality(nationality);
        customer.setAddress(address);
        customer.setPermissions(new HashSet<>(getPermissionForRenter()));
        if (customerRepository.findByEmail(customer.getEmail()).isEmpty()) {
            customerRepository.save(customer);
        }else {
            customer = customerRepository.findByEmail(customer.getEmail()).get();
        }
//        customerRepository.save(customer);
        return customer;
    }

    private static final List<String> ALL_AMENITIES = Arrays.asList(
            "24hr front desk", "air-conditioned", "fitness", "pool", "sauna", "spa", "bar", "restaurant", "wi-fi", "pet-friendly",
            "family rooms", "room service", "concierge service", "laundry service", "fitness center", "non-smoking rooms",
            "outdoor pool", "indoor pool", "business center", "conference rooms", "meeting facilities", "breakfast buffet",
            "private beach", "hot tub", "massage", "all-inclusive", "casino", "airport transfer", "elevator", "balcony/terrace",
            "kitchenette"
    );

    private List<String> getRandomAmenities() {
        Random random = new Random();
        Set<String> selectedAmenities = new HashSet<>();

        while (selectedAmenities.size() < 8) {
            String randomAmenity = ALL_AMENITIES.get(random.nextInt(ALL_AMENITIES.size()));
            selectedAmenities.add(randomAmenity);
        }

        return new ArrayList<>(selectedAmenities);
    }

    private List<Permission> getPermissionForRenter(){
        try{
            List<Permission> resultList = new ArrayList<>();
            for(String s : Constants.userPermissions.get(Constants.RENTER)){
                Optional<Permission> p = permissionRepository.findByName(s);
                if(p.isPresent()) {
                    resultList.add(p.get());
                }
            }

            return resultList;
        }catch(Exception e){
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    protected Property createProperty(String title, String country, String city, String address, String postalCode, String latitude, String longitude, String folderName, Customer owner) throws IOException {
        Property property = new Property();
        property.setTitle(title);
        property.setCountry(country);
        property.setCity(city);
        property.setAddress(address);
        property.setPostalCode(postalCode);
        property.setLatitude(latitude);
        property.setLongitude(longitude);
        property.setAmenities(getRandomAmenities());

        property.setCheckIn("14:00");
        property.setCheckOut("10:00");
        property.setHighlights(Arrays.asList("Beachfront", "Free WiFi", "Terrace", "Garden", "Private beach area"));
        property.setDescription("Located in the heart of Chania Old Town, Riviera Superior offers a luxurious stay with stunning sea views. The property features a private beach area, a garden, and a terrace. Free WiFi is available throughout the property.\n" +
                "\n" +
                "All rooms are air-conditioned and come with a flat-screen TV, a kettle, a shower, a hairdryer, and a desk. The rooms also feature a wardrobe and a private bathroom.\n" +
                "\n" +
                "Guests at Riviera Superior can enjoy a continental or a buffet breakfast.\n" +
                "\n" +
                "Popular points of interest near the accommodation include Koum Kapi Beach, Nea Chora Beach, and Golden Beach. The nearest airport is Chania International Airport, 14 km from Riviera Superior.");

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


    private void generateDailyPricesForYear(Property property, Integer year) {
        Random random = new Random();
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            double randomPrice = 30 + (500 - 30) * random.nextDouble();

            int consecutiveDays = random.nextInt(5) + 1;

            for (int i = 0; i < consecutiveDays && !currentDate.isAfter(endDate); i++) {
                DailyPrice dailyPrice = new DailyPrice();
                dailyPrice.setDate(currentDate);
                dailyPrice.setPrice(randomPrice);
                dailyPrice.setProperty(property);
                dailyPriceRepository.save(dailyPrice);

                currentDate = currentDate.plusDays(1);
            }
        }
    }

    private List<String> copyImagesToUploads(String propertyFolderName) throws IOException {
        List<String> uploadedImagePaths = new ArrayList<>();

        Path propertyImagesPath = Paths.get(imagesDir + propertyFolderName);

        Path propertyUploadsPath = Paths.get(uploadsDir + propertyFolderName);
        if (!Files.exists(propertyUploadsPath)) {
            Files.createDirectories(propertyUploadsPath);
        }

        try (Stream<Path> stream = Files.list(propertyImagesPath)) {
            uploadedImagePaths = stream
                    .filter(Files::isRegularFile)
                    .map(imageFile -> {
                        try {
                            String newFileName = UUID.randomUUID().toString() + "-" + imageFile.getFileName().toString();
                            Path targetPath = propertyUploadsPath.resolve(newFileName);

                            Files.copy(imageFile, targetPath, StandardCopyOption.REPLACE_EXISTING);

                            return uploadsDir + propertyFolderName + "/" + newFileName;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(path -> path != null)
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
