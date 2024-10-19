package rs.edu.raf.rentinn.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PropertyDto {

    private Long id;
    private String title;
    private String city;
    private String country;
    private String address;
    private String postalCode;
    private String latitude;
    private String longitude;
    private List<String> imagePaths;
    private List<DailyPriceDto> dailyPrices;

}

