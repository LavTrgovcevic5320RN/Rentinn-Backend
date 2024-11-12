package rs.edu.raf.rentinn.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.edu.raf.rentinn.dtos.PropertyDto;
import rs.edu.raf.rentinn.mapper.PropertyMapper;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.model.DailyPrice;
import rs.edu.raf.rentinn.model.Property;
import rs.edu.raf.rentinn.repositories.CustomerRepository;
import rs.edu.raf.rentinn.repositories.DailyPriceRepository;
import rs.edu.raf.rentinn.repositories.PropertyRepository;
import rs.edu.raf.rentinn.requests.CreatePropertyRequest;
import rs.edu.raf.rentinn.requests.DailyPriceRequest;
import rs.edu.raf.rentinn.services.PropertyService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final CustomerRepository customerRepository;
    private final DailyPriceRepository dailyPriceRepository;
    private final PropertyMapper propertyMapper;
    private final String uploadDir = "uploads/";
    private static final Logger logger = LoggerFactory.getLogger(PropertyServiceImpl.class);


    public PropertyServiceImpl(
            PropertyRepository propertyRepository,
            CustomerRepository customerRepository,
            DailyPriceRepository dailyPriceRepository,
            PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.customerRepository = customerRepository;
        this.dailyPriceRepository = dailyPriceRepository;
        this.propertyMapper = propertyMapper;
    }

    @Override
    public List<PropertyDto> getAllProperties() {
        return propertyRepository.findAll().stream().map(propertyMapper::propertyToPropertyDto).toList();
    }

    @Override
    public PropertyDto getPropertyById(Long id) {
        return propertyRepository.findById(id).map(propertyMapper::propertyToPropertyDto).orElse(null);
    }

    @Override
    public Property saveProperty(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public boolean createProperty(CreatePropertyRequest request, List<MultipartFile> images) throws IOException {
        Property property = new Property();
        property.setOwner(customerRepository.findByUserId(request.getOwnerId()).orElse(null));
        property.setTitle(request.getTitle());
        property.setCountry(request.getCountry());
        property.setCity(request.getCity());
        property.setAddress(request.getAddress());
        property.setPostalCode(request.getPostalCode());
        property.setLatitude(request.getLatitude());
        property.setLongitude(request.getLongitude());
        property.setHighlights(request.getHighlights());
        property.setAmenities(request.getAmenities());
        property.setFreebies(request.getFreebies());
        property.setCheckIn(request.getCheckIn());
        property.setCheckOut(request.getCheckOut());
        property.setDescription(request.getDescription());
        List<String> imagePaths = new ArrayList<>();

        String propertyDir = uploadDir + request.getTitle();
        Path propertyDirPath = Paths.get(propertyDir);

        if (!Files.exists(propertyDirPath)) {
            Files.createDirectories(propertyDirPath);
        }

        for (MultipartFile image : images) {
            String uniqueFilename = UUID.randomUUID() + ".jpg";
            Path imagePath = propertyDirPath.resolve(uniqueFilename);
            Files.write(imagePath, image.getBytes());

            String imageUrl = "/uploads/" + request.getTitle().replace(" ", "%20") + "/" + uniqueFilename;
            imagePaths.add(imageUrl);
        }

        property.setImagePaths(imagePaths);

        property = propertyRepository.save(property);

        List<DailyPrice> dailyPrices = new ArrayList<>();

        for(DailyPriceRequest dailyPriceRequest : request.getDailyPrices()) {
            DailyPrice dailyPrice = new DailyPrice();
            dailyPrice.setPrice(dailyPriceRequest.getPrice());
            dailyPrice.setDate(LocalDate.parse(dailyPriceRequest.getDate()));
            dailyPrice.setProperty(property);
            dailyPrices.add(dailyPrice);
            dailyPriceRepository.save(dailyPrice);
        }

        property.setDailyPrices(dailyPrices);
        propertyRepository.save(property);

        return true;
    }

    @Override
    public List<PropertyDto> getFavoritePropertiesByUserId(Customer customer) {
        return propertyRepository.findFavoritePropertiesByUserId(customer.getFavoriteProperties()).stream().map(propertyMapper::propertyToPropertyDto).toList();
    }

}
