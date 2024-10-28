package rs.edu.raf.rentinn.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.rentinn.dtos.BookingDto;
import rs.edu.raf.rentinn.model.Booking;

@Component
public class BookingMapper {

    private ReviewMapper reviewMapper;

    public BookingMapper(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    public BookingDto bookingToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setCheckInDate(booking.getCheckInDate());
        bookingDto.setCheckOutDate(booking.getCheckOutDate());
        bookingDto.setTotalPrice(booking.getTotalPrice());
        bookingDto.setPropertyName(booking.getProperty().getTitle());
        bookingDto.setCustomerId(booking.getCustomer().getUserId());
        bookingDto.setPropertyId(booking.getProperty().getId());
        bookingDto.setReview(booking.getReview() != null ? reviewMapper.reviewToReviewDto(booking.getReview()) : null);

        return bookingDto;
    }
}
