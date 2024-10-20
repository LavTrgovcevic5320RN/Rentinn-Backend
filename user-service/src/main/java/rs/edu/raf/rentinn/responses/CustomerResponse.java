package rs.edu.raf.rentinn.responses;

import lombok.Getter;
import lombok.Setter;
import rs.edu.raf.rentinn.dtos.PermissionDto;
import rs.edu.raf.rentinn.dtos.PropertyDto;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class CustomerResponse {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private List<PermissionDto> permissions;
    private List<PropertyDto> properties;
//    private Long dateOfBirth;
//    private String gender;
//    private String address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerResponse that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(permissions, that.permissions) && Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, permissions, properties);
    }
}
