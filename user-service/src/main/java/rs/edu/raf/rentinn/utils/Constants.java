package rs.edu.raf.rentinn.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {
    public static final String ALL_POSITIONS = "All";
    public static final Integer BEARER_PREFIX_SIZE = 7;
    public static final Long JWT_EXPIRATION_LENGTH = 1000L * 60 * 60 * 10;
    public static final Long SINGLE_USE_CODE_EXPIRATION_LENGTH = 1000L * 60 * 5;
    public static final String ADMIN = "admin";

    public static final List<String> allPermissions = Arrays.asList(
            "addUser", "modifyUser", "deleteUser", "readUser", "modifyCustomer");

    public static final Map<String, List<String>> userPermissions = new HashMap<String, List<String>>(){{
//        put(AGENT, Arrays.asList("addUser", "modifyUser", "deleteUser", "readUser", "modifyCustomer"));
//        put(SUPERVIZOR, allPermissions);
        put(ADMIN, allPermissions);
    }};
}
