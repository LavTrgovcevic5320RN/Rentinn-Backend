package rs.edu.raf.rentinn.services;

import org.springframework.web.multipart.MultipartFile;
import rs.edu.raf.rentinn.dtos.PropertyDto;
import rs.edu.raf.rentinn.model.Customer;
import rs.edu.raf.rentinn.model.Property;
import rs.edu.raf.rentinn.requests.CreatePropertyRequest;

import java.io.IOException;
import java.util.List;

public interface PropertyService {

    List<PropertyDto> getAllProperties();

    PropertyDto getPropertyById(Long id);

    Property saveProperty(Property property);

    boolean createProperty(CreatePropertyRequest property, List<MultipartFile> images) throws IOException;

    List<PropertyDto> getFavoritePropertiesByUserId(Customer customer);
}
