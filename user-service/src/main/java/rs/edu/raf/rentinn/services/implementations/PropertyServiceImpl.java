package rs.edu.raf.rentinn.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.edu.raf.rentinn.dtos.PropertyDto;
import rs.edu.raf.rentinn.mapper.PropertyMapper;
import rs.edu.raf.rentinn.model.Property;
import rs.edu.raf.rentinn.repositories.PropertyRepository;
import rs.edu.raf.rentinn.services.PropertyService;

import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    public PropertyServiceImpl(
            PropertyRepository propertyRepository,
            PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
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


}
