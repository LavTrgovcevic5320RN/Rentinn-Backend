package rs.edu.raf.rentinn.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.rentinn.dtos.ReviewDto;
import rs.edu.raf.rentinn.model.Review;
import rs.edu.raf.rentinn.repositories.CustomerRepository;

@Component
public class ReviewMapper {

    private CustomerRepository customerRepository;

    public ReviewMapper(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public ReviewDto reviewToReviewDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        if(customerRepository.findById(review.getCustomer().getUserId()).isPresent()){
            String firstName = customerRepository.findById(review.getCustomer().getUserId()).get().getFirstName();
            String lastName = customerRepository.findById(review.getCustomer().getUserId()).get().getLastName();
            reviewDto.setName(firstName + " " + lastName);
        }
        reviewDto.setRating(review.getRating());
        reviewDto.setComment(review.getComment());

        return reviewDto;
    }
}
