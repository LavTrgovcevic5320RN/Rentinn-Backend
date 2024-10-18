package rs.edu.raf.rentinn.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.rentinn.dtos.EmployeeDto;
import rs.edu.raf.rentinn.dtos.PermissionDto;
import rs.edu.raf.rentinn.model.Employee;
import rs.edu.raf.rentinn.model.Permission;
import rs.edu.raf.rentinn.repositories.PermissionRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeMapper {
    private PermissionMapper permissionMapper;
    private PasswordEncoder passwordEncoder;
    private PermissionRepository permissionRepository;

    public EmployeeMapper(PermissionMapper permissionMapper,
                          PasswordEncoder passwordEncoder,
                          PermissionRepository permissionRepository) {
        this.permissionMapper = permissionMapper;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
    }

    public EmployeeDto employeeToEmployeeDto(Employee employee) {
        if(employee == null) return null;
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setUserId(employee.getUserId());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setJmbg(employee.getJmbg());
        employeeDto.setPhoneNumber(employee.getPhoneNumber());
        employeeDto.setActive(employee.getActive());

        List<PermissionDto> permissionDtoList = new ArrayList<>();

        for (Permission permission : employee.getPermissions()) {
            permissionDtoList.add(permissionMapper.permissionToPermissionDto(permission));
        }

        employeeDto.setPermissions(permissionDtoList);

        return employeeDto;
    }
}
