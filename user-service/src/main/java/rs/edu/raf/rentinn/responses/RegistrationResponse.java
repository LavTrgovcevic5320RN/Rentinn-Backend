package rs.edu.raf.rentinn.responses;

import lombok.Data;

import java.util.List;

@Data
public class RegistrationResponse {

    private String jwt;

    private List<String> permissions;

    public RegistrationResponse(String jwt, List<String> permissions) {
        this.jwt = jwt;
        this.permissions = permissions;
    }
}
