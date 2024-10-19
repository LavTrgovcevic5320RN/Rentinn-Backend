package rs.edu.raf.rentinn.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

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

    @ElementCollection
    @Column(name = "image_paths")
    private List<String> imagePaths;

    @ElementCollection
    @Column
    private List<String> amenities;

    @ElementCollection
    @Column
    private List<String> freebies;

    @Column
    private Double rating;

    @Column
    private String description;

    @Column
    @ElementCollection
    private List<String> highlights;

    @Column
    @ElementCollection
    private List<String> houseRules;

    @Column
    private String checkIn;

    @Column
    private String checkOut;



    @Column
    private Integer numberOfRooms;

    @Column
    private Integer maxGuests;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Customer owner;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<DailyPrice> dailyPrices = new ArrayList<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
}
