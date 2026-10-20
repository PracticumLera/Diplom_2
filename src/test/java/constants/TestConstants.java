package constants;

public class TestConstants {

    public static class HttpStatus {
        public static final int OK = 200;
        public static final int BAD_REQUEST = 400;
        public static final int UNAUTHORIZED = 401;
        public static final int INTERNAL_SERVER_ERROR = 500;
        public static final int FORBIDDEN =403;
    }

    public static class User {
        public static final String EMAIL_PREFIX = "user_";
        public static final String EMAIL_DOMAIN = "@yandex.ru";
        public static final String DEFAULT_PASSWORD = "SecurePassword123!";
        public static final String DEFAULT_FIRST_NAME = "TestUser";
        public static final String INCORRECT_PASSWORD = "WrongPassword456!";
    }

    public static class Ingredient {
        public static final String INVALID_HASH = "invalid_hash_123";
        public static final String NON_EXISTENT_ID = "60d3b41abdacab026a733c7";
    }

    public static class Error {
        public static final String USER_ALREADY_EXISTS = "User already exists";
        public static final String REQUIRED_FIELDS = "Email, password and name are required fields";
        public static final String INVALID_CREDENTIALS = "email or password are incorrect";
        public static final String INGREDIENTS_REQUIRED = "Ingredient ids must be provided";
        public static final String NOT_AUTHORIZED = "You should be authorised";
    }

    public static class JsonKeys {
        public static final String SUCCESS = "success";
        public static final String USER = "user";
        public static final String EMAIL = "email";
        public static final String NAME = "name";
        public static final String ACCESS_TOKEN = "accessToken";
        public static final String REFRESH_TOKEN = "refreshToken";
        public static final String ORDER = "order";
        public static final String ORDER_NUMBER = "order.number";
        public static final String ORDER_ID = "order._id";
        public static final String MESSAGE = "message";
    }
}
