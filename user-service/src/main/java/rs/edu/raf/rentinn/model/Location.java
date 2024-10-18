package rs.edu.raf.rentinn.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "locationId")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

//    @OneToOne(mappedBy = "location")
//    @OneToOne
//    @JoinColumn(name = "property_id", referencedColumnName = "propertyId")
//    private Property property;
}
