package rs.edu.raf.rentinn.mapper;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rs.edu.raf.rentinn.dtos.PropertyDto;
import rs.edu.raf.rentinn.model.Property;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PropertyMapper {

    private DailyPriceMapper dailyPriceMapper;

    public PropertyMapper(DailyPriceMapper dailyPriceMapper) {
        this.dailyPriceMapper = dailyPriceMapper;
    }

    public PropertyDto propertyToPropertyDto(Property property) {
        PropertyDto propertyDto = new PropertyDto();
        propertyDto.setId(property.getId());
        propertyDto.setTitle(property.getTitle());
        propertyDto.setCity(property.getCity());
        propertyDto.setCountry(property.getCountry());
        propertyDto.setAddress(property.getAddress());
        propertyDto.setPostalCode(property.getPostalCode());
        propertyDto.setLatitude(property.getLatitude());
        propertyDto.setLongitude(property.getLongitude());
        propertyDto.setDailyPrices(property.getDailyPrices().stream().map(dailyPriceMapper::dailyPriceToDailyPriceDto).toList());

        List<String> imageUrls = property.getImagePaths().stream()
                .map(imagePath -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/" + imagePath)
                        .toUriString())
                .collect(Collectors.toList());

        propertyDto.setImagePaths(imageUrls);

        return propertyDto;
    }
}
