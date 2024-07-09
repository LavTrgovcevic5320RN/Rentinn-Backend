package rs.edu.raf.rentinn.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "userId")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long userId;

    @Column(unique = true)
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Column
    @NotBlank(message = "Password is mandatory")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true)
    private String jmbg;

    @Column
    private String phoneNumber;

    @Column(nullable = false)
    private Boolean active;

    @Column
    private String activationToken;

    @Column
    private String resetPasswordToken;

    @Override
    public String toString() {
        return STR."User{userId=\{userId}, email='\{email}\{'\''}, password='\{password}\{'\''}, firstName='\{firstName}\{'\''}, lastName='\{lastName}\{'\''}, jmbg='\{jmbg}\{'\''}, phoneNumber='\{phoneNumber}\{'\''}, active=\{active}, activationToken='\{activationToken}\{'\''}, resetPasswordToken='\{resetPasswordToken}\{'\''}\{'}'}";
    }
}
