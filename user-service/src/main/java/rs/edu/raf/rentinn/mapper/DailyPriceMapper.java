package rs.edu.raf.rentinn.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.rentinn.dtos.DailyPriceDto;
import rs.edu.raf.rentinn.model.DailyPrice;

@Component
public class DailyPriceMapper {

    public DailyPriceDto dailyPriceToDailyPriceDto(DailyPrice dailyPrice) {
        DailyPriceDto dailyPriceDto = new DailyPriceDto();
        dailyPriceDto.setId(dailyPrice.getId());
        dailyPriceDto.setDate(dailyPrice.getDate());
        dailyPriceDto.setPrice(dailyPrice.getPrice());

        return dailyPriceDto;
    }
}
