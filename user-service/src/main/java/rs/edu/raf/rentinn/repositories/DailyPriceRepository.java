package rs.edu.raf.rentinn.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.edu.raf.rentinn.model.DailyPrice;

import java.time.LocalDate;
import java.util.List;

public interface DailyPriceRepository extends JpaRepository<DailyPrice, Long> {

    @Transactional
    @Query("SELECT dp FROM DailyPrice dp WHERE dp.property.id = :propertyId AND dp.date BETWEEN :startDate AND :endDate")
    List<DailyPrice> findPricesByDateRange(@Param("propertyId") Long propertyId,
                                           @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

}
