package rs.edu.raf.rentinn.services;

import rs.edu.raf.rentinn.dtos.PropertyDto;
import rs.edu.raf.rentinn.model.Property;

import java.util.List;

public interface PropertyService {

    List<PropertyDto> getAllProperties();


    Property saveProperty(Property property);
}