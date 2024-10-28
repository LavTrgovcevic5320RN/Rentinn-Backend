package rs.edu.raf.rentinn.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePropertyRequest {

    private Long ownerId;
    private String title;
    private String country;
    private String city;
    private String address;
    private String postalCode;
    private String latitude;
    private String longitude;
    private List<String> amenities;
    private List<String> freebies;
    private List<DailyPriceRequest> dailyPrices;
    private String description;
    private List<String> highlights;
    private String checkIn;
    private String checkOut;
    private double totalPrice;
}
