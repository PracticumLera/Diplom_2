package constants;

public class Config {

    // Environment
    private static final String ENV = System.getProperty("env", "prod");

    // Base URL
    public static final String BASE_URL = getBaseUrl();
    private static final String API_PREFIX = BASE_URL + "/api";

    // API Endpoints
    public static final String REGISTER_URL = API_PREFIX + "/auth/register";
    public static final String LOGIN_URL = API_PREFIX + "/auth/login";
    public static final String USER_URL = API_PREFIX + "/auth/user";
    public static final String ORDER_URL = API_PREFIX + "/orders";
    public static final String INGREDIENT_URL = API_PREFIX + "/ingredients";

    // Headers
    public static final String AUTH_HEADER = "Authorization";

    private static String getBaseUrl() {
        switch (ENV) {
            case "dev":
                return "https://dev.stellarburgers.education-services.ru";
            case "staging":
                return "https://staging.stellarburgers.education-services.ru";
            default:
                return "https://stellarburgers.education-services.ru";
        }
    }
}