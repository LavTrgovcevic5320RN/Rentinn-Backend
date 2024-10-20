package rs.edu.raf.rentinn.mapper;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rs.edu.raf.rentinn.dtos.PropertyDto;
import rs.edu.raf.rentinn.model.Property;
import rs.edu.raf.rentinn.model.Review;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PropertyMapper {

    private DailyPriceMapper dailyPriceMapper;
    private ReviewMapper reviewMapper;

    public PropertyMapper(DailyPriceMapper dailyPriceMapper,
                          ReviewMapper reviewMapper) {
        this.dailyPriceMapper = dailyPriceMapper;
        this.reviewMapper = reviewMapper;
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
        propertyDto.setCheckIn(property.getCheckIn());
        propertyDto.setCheckOut(property.getCheckOut());
        propertyDto.setDescription(property.getDescription());
        propertyDto.setHighlights(property.getHighlights());
        propertyDto.setAmenities(property.getAmenities());
        propertyDto.setFreebies(property.getFreebies());
        propertyDto.setReviews(property.getReviews().stream().map(reviewMapper::reviewToReviewDto).toList());
        propertyDto.setRating(property.getReviews().stream().mapToDouble(Review::getRating).average().orElse(0));
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
