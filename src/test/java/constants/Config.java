package constants;

public class Config {

    public static final String BASE_URL = "https://qa-stellarburgers.education-services.ru";
    private static final String API_PREFIX = BASE_URL + "/api";

    public static final String REGISTER_URL = API_PREFIX + "/auth/register";
    public static final String LOGIN_URL = API_PREFIX + "/auth/login";
    public static final String USER_URL = API_PREFIX + "/auth/user";
    public static final String ORDER_URL = API_PREFIX + "/orders";
    public static final String INGREDIENT_URL = API_PREFIX + "/ingredients";

    public static final String AUTH_HEADER = "Authorization";
}