package rs.edu.raf.rentinn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.edu.raf.rentinn.model.Booking;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.property.id = :propertyId AND " +
            "(b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate)")
    List<Booking> findOverlappingBookings(@Param("propertyId") Long propertyId,
                                          @Param("checkInDate") LocalDate checkInDate,
                                          @Param("checkOutDate") LocalDate checkOutDate);

}