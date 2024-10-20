package rs.edu.raf.rentinn.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {

    private Long id;
    private String name;
    private String comment;
    private Double rating;
}
