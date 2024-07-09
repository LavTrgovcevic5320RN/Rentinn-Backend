package rs.edu.raf.rentinn.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "permissionId")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @Column
    private String name;

    @Column
    private String description;

//    @ManyToMany(mappedBy = "permissions")
//    private Set<User> users = new HashSet<>();

    @ManyToMany(mappedBy = "permissions")
    private Set<Employee> employees = new HashSet<>();

    @ManyToMany(mappedBy = "permissions")
    private Set<Customer> customers = new HashSet<>();
}
