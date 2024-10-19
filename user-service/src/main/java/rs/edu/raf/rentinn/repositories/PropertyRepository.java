package rs.edu.raf.rentinn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.edu.raf.rentinn.model.Property;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p " +
            "WHERE p.city = :city " +
            "AND p.country = :country")
    List<Property> findByLocation(@Param("city") String city, @Param("country") String country);
}

