package rs.edu.raf.rentinn.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {
    public static final Integer BEARER_PREFIX_SIZE = 7;
    public static final Long JWT_EXPIRATION_LENGTH = 1000L * 60 * 60 * 10;
    public static final Long SINGLE_USE_CODE_EXPIRATION_LENGTH = 1000L * 60 * 5;
    public static final String ADMIN = "admin";
    public static final String OWNER = "owner";
    public static final String RENTER = "renter";


    public static final List<String> allPermissions = Arrays.asList(
            "addUser", "modifyUser", "deleteUser", "readUser", "modifyCustomer",
            "rentProperty", "readProperty", "addProperty",
            "modifyProperty", "deleteProperty"
    );

    public static final List<String> ownerPermissions = Arrays.asList(
            "rentProperty", "readProperty", "addProperty",
            "modifyProperty", "deleteProperty"
    );

    public static final List<String> renterPermissions = Arrays.asList(
            "rentProperty", "readProperty", "addProperty"
            );

    public static final Map<String, List<String>> userPermissions = new HashMap<>() {{
        put(ADMIN, allPermissions);
        put(OWNER, ownerPermissions);
        put(RENTER, renterPermissions);
    }};
}
