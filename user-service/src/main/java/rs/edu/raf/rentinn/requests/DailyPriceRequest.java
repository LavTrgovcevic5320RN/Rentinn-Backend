package rs.edu.raf.rentinn.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyPriceRequest {

    private String date;
    private double price;
}
