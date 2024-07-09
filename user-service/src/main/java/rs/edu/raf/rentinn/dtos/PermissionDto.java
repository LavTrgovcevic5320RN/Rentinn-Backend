package rs.edu.raf.rentinn.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionDto {
    private Long permissionId;

    private String name;

    private String description;
}
