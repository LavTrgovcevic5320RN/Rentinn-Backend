package rs.edu.raf.rentinn.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeDto {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String jmbg;
    private String position;
    private String phoneNumber;
    private Boolean active;
    private Double limitNow;
    private Double orderlimit;
    private Boolean requireApproval;
    private List<PermissionDto> permissions;
}
