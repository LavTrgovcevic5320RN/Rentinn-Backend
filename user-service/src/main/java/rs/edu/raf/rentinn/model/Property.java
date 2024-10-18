package rs.edu.raf.rentinn.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "propertyId")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

//    @ManyToOne
//    @JoinColumn(name = "customer_id", referencedColumnName = "userId")
//    private Customer customer;


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

    // List of image file paths
    @ElementCollection
    @Column(name = "image_paths")
    private List<String> imagePaths;

  //  //    @OneToOne(fetch = FetchType.EAGER)
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "location_id", referencedColumnName = "locationId")
//    private Location location;

}

