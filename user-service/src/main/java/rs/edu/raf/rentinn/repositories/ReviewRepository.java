package rs.edu.raf.rentinn.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.rentinn.model.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Transactional
    List<Review> findAllByPropertyId(Long propertyId);
}
