package rs.edu.raf.rentinn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.rentinn.model.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}

