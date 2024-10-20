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

    @Override
    public void run(String... args) throws Exception {
//        seedPermissions();
//        System.out.println("Permissions seeded");
//
//        seedUsers();
//        System.out.println("Users seeded");
//
//        seedProperties();
//        System.out.println("Properties seeded");

        Customer customer1 = new Customer();
        customer1.setEmail("customer@gmail.com");
        customer1.setPassword(passwordEncoder.encode("Markovic01@"));
        customer1.setFirstName("Marko");
        customer1.setLastName("Markovic");
        customer1.setJmbg("1234567890123");
        customer1.setPhoneNumber("123456789");
        customer1.setActive(true);
        customer1.setDateOfBirth(LocalDate.of(1985, 5, 10).toEpochDay());
        customer1.setGender("Male");
        customer1.setNationality("USA");
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setEmail("admin@gmail.com");
        customer2.setPassword(passwordEncoder.encode("Markovic01@"));
        customer2.setFirstName("Jane");
        customer2.setLastName("Smith");
        customer2.setJmbg("9876543210987");
        customer2.setPhoneNumber("987654321");
        customer2.setActive(true);
        customer2.setDateOfBirth(LocalDate.of(1990, 3, 22).toEpochDay());
        customer2.setGender("Female");
        customer2.setNationality("Canada");
        customerRepository.save(customer2);

        Customer customer3 = new Customer();
        customer3.setEmail("renter@example.com");
        customer3.setPassword(passwordEncoder.encode("Markovic01@"));
        customer3.setFirstName("Alex");
        customer3.setLastName("Johnson");
        customer3.setJmbg("5647382910123");
        customer3.setPhoneNumber("5647382910");
        customer3.setActive(true);
        customer3.setDateOfBirth(LocalDate.of(1995, 8, 15).toEpochDay());
        customer3.setGender("Non-binary");
        customer3.setNationality("UK");
        customerRepository.save(customer3);

        System.out.println("Customers seeded");



        Property property1 = new Property();
        property1.setTitle("Riviera Superior");
        property1.setCountry("Greece");
        property1.setCity("Crete");
        property1.setAddress("Chania Old Town");
        property1.setPostalCode("73132");
        property1.setLatitude("35.5184506");
        property1.setLongitude("24.0226517");

        String folderName1 = "Riviera Superior Sweet Sea Front";
        List<String> imagePaths1 = copyImagesToUploads(folderName1);
        property1.setImagePaths(imagePaths1);

        property1.setOwner(customer1);
        propertyRepository.save(property1);

        Property property2 = new Property();
        property2.setTitle("Riviera Deluxe");
        property2.setCountry("Greece");
        property2.setCity("Crete");
        property2.setAddress("Chania Old Town");
        property2.setPostalCode("73132");
        property2.setLatitude("35.5184506");
        property2.setLongitude("24.0226517");

        String folderName2 = "Riviera Deluxe Suite Sea Front";
        List<String> imagePaths2 = copyImagesToUploads(folderName2);
        property2.setImagePaths(imagePaths2);

        property2.setOwner(customer1);
        propertyRepository.save(property2);

        Property property3 = new Property();
        property3.setTitle("Sagara Candidasa Classic");
        property3.setCountry("Indonesia");
        property3.setCity("Bali");
        property3.setAddress("Candidasa");
        property3.setPostalCode("80851");
        property3.setLatitude("-8.5100184");
        property3.setLongitude("115.5704357");

        String folderName3 = "Sagara Candidasa Classic Room";
        List<String> imagePaths3 = copyImagesToUploads(folderName3);
        property3.setImagePaths(imagePaths3);

        property3.setOwner(customer2);
        propertyRepository.save(property3);

        System.out.println("Properties seeded");


        generateDailyPricesForYear(property1);
        generateDailyPricesForYear(property2);
        generateDailyPricesForYear(property3);

        System.out.println("Daily prices seeded");


        LocalDate checkInDate = LocalDate.of(2024, 10, 18);
        LocalDate checkOutDate = LocalDate.of(2024, 10, 20);

        Booking booking1 = new Booking();
        booking1.setCheckInDate(checkInDate);
        booking1.setCheckOutDate(checkOutDate);
        booking1.setTotalPrice(
                dailyPriceRepository
                        .findPricesByDateRange(property1.getId(), checkInDate, checkOutDate)
                        .stream()
                        .mapToDouble(DailyPrice::getPrice)
                        .sum());
        booking1.setCustomer(customer3);
        booking1.setProperty(property1);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setCheckInDate(LocalDate.of(2024, 11, 5));
        booking2.setCheckOutDate(LocalDate.of(2024, 11, 10));
        booking2.setTotalPrice(120.0);
        booking2.setCustomer(customer3);
        booking2.setProperty(property3);
        bookingRepository.save(booking2);

        System.out.println("Bookings seeded");



        Review review1 = new Review();
        review1.setComment("Amazing stay at the Riviera Superior!");
        review1.setRating(5);
        review1.setCustomer(customer3);  // Recenzent je isti korisnik koji je iznajmljivao
        review1.setProperty(property1);
        reviewRepository.save(review1);

        Review review2 = new Review();
        review2.setComment("Very cozy place, but could improve the internet speed.");
        review2.setRating(4);
        review2.setCustomer(customer3);
        review2.setProperty(property3);
        reviewRepository.save(review2);

        System.out.println("Reviews seeded");

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

    private void seedProperties() {
        Property property1 = new Property();
        property1.setTitle("Riviera Superior");
//        property2.setDescription("Description " + i);
        property1.setCountry("Greece");
        property1.setCity("Crete");
        property1.setAddress("Chania Old Town");
        property1.setPostalCode("73132");
        property1.setLatitude("35.5184506");
        property1.setLongitude("24.0226517");
        property1.setImagePaths(new ArrayList<>());
        propertyRepository.save(property1);

        Property property2 = new Property();
        property2.setTitle("Riviera Deluxe");
//        property2.setDescription("Description " + i);
        property2.setCountry("Greece");
        property2.setCity("Crete");
        property2.setAddress("Chania Old Town");
        property2.setPostalCode("73132");
        property2.setLatitude("35.5184506");
        property2.setLongitude("24.0226517");
        property2.setImagePaths(new ArrayList<>());
        propertyRepository.save(property2);

        Property property3 = new Property();
        property3.setTitle("Riviera Executive");
//        property3.setDescription("Description " + i);
        property3.setCountry("Greece");
        property3.setCity("Crete");
        property3.setAddress("Chania Old Town");
        property3.setPostalCode("73132");
        property3.setLatitude("35.5184506");
        property3.setLongitude("24.0226517");
        property3.setImagePaths(new ArrayList<>());
        propertyRepository.save(property3);

        Property property4 = new Property();
        property4.setTitle("Riviera Classic");
//        property4.setDescription("Description " + i);
        property4.setCountry("Greece");
        property4.setCity("Crete");
        property4.setAddress("Chania Old Town");
        property4.setPostalCode("73132");
        property4.setLatitude("35.5184506");
        property4.setLongitude("24.0226517");
        property4.setImagePaths(new ArrayList<>());
        propertyRepository.save(property4);

        Property property5 = new Property();
        property5.setTitle("Sagara Candidasa Classic");
//        property5.setDescription("Description " + i);
        property5.setCountry("Indonesia");
        property5.setCity("Bali");
        property5.setAddress("Candidasa");
        property5.setPostalCode("80851");
        property5.setLatitude("-8.5100184");
        property5.setLongitude("115.5704357");
        property5.setImagePaths(new ArrayList<>());
        propertyRepository.save(property5);

        Property property6 = new Property();
        property6.setTitle("Sagara Candidasa Premiere");
//        property6.setDescription("Description " + i);
        property6.setCountry("Indonesia");
        property6.setCity("Bali");
        property6.setAddress("Candidasa");
        property6.setPostalCode("80851");
        property6.setLatitude("-8.5100184");
        property6.setLongitude("115.5704357");
        property6.setImagePaths(new ArrayList<>());
        propertyRepository.save(property6);

        Property property7 = new Property();
        property7.setTitle("Genggong");
//        property7.setDescription("Description " + i);
        property7.setCountry("Indonesia");
        property7.setCity("Bali");
        property7.setAddress("Candidasa");
        property7.setPostalCode("80851");
        property7.setLatitude("-8.513122693684073");
        property7.setLongitude("115.5761964819635");
        property7.setImagePaths(new ArrayList<>());
        propertyRepository.save(property7);

        Property property8 = new Property();
        property8.setTitle("Sagara Candidasa Suite");

    }

    private void seedUsers() {
        for(int i = 0; i < 10; i++) {
            Employee employee = generateEmployee(
                    STR."admin\{i}@gmail.com",
                    "Markovic01@",
                    "Employee",
                    "Markovic",
                    Constants.ADMIN,
                    true
            );

            Customer customer = generateCustomer(
                    "customer" + i + "@gmail.com",
                    "Markovic01@",
                    "Customer",
                    "Markovic",
                    Constants.RENTER,
                    true
            );
        }
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
