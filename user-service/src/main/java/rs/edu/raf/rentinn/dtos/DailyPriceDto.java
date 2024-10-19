package rs.edu.raf.rentinn.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DailyPriceDto {

    private Long id;
    private LocalDate date;
    private Double price;

}
