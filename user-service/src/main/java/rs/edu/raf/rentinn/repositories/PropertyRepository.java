package rs.edu.raf.rentinn.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.edu.raf.rentinn.model.Property;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Transactional
    @Query("SELECT p FROM Property p WHERE (p.city LIKE %:city1% AND p.country LIKE %:country1%) OR (p.country LIKE %:country2% AND p.city LIKE %:city2%)")
    List<Property> findByCityAndCountryOrCountryAndCity(@Param("city1") String city1, @Param("country1") String country1,
                                                        @Param("country2") String country2, @Param("city2") String city2);

    @Transactional
    @Query("SELECT p FROM Property p WHERE p.city LIKE %:location% OR p.country LIKE %:location%")
    List<Property> findByCityOrCountry(@Param("location") String location);

    @Transactional
    @Query("SELECT p FROM Property p WHERE p.id IN :favoritePropertyIds")
    List<Property> findFavoritePropertiesByUserId(@Param("favoritePropertyIds") List<Long> favoritePropertyIds);

}

