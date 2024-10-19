package rs.edu.raf.rentinn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String comment;

    @Column
    private int rating;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;
}

