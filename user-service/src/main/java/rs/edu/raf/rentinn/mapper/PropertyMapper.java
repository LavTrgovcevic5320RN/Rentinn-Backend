package rs.edu.raf.rentinn.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.rentinn.dtos.PropertyDto;
import rs.edu.raf.rentinn.model.Property;

@Component
public class PropertyMapper {

    public PropertyDto propertyToPropertyDto(Property property) {
        return new PropertyDto();
    }
}
