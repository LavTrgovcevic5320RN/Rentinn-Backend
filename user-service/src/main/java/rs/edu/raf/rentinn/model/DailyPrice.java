package rs.edu.raf.rentinn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class DailyPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private Double price;


    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;
}

