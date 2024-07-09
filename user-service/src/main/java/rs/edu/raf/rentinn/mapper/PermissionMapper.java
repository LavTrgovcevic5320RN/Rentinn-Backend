package rs.edu.raf.rentinn.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.rentinn.dtos.PermissionDto;
import rs.edu.raf.rentinn.model.Permission;

@Component
public class PermissionMapper {

    public PermissionDto permissionToPermissionDto(Permission permission) {
        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setPermissionId(permission.getPermissionId());
        permissionDto.setName(permission.getName());
        permissionDto.setDescription(permission.getDescription());
        return permissionDto;
    }

    public Permission permissionDtoToPermission(PermissionDto permissionDto) {
        Permission permission = new Permission();
        permission.setPermissionId(permissionDto.getPermissionId());
        permission.setName(permissionDto.getName());
        permission.setDescription(permissionDto.getDescription());
        return permission;
    }
}
