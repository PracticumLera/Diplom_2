package tests;

import base.BaseApiTest;
import constants.TestConstants;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import java.util.UUID;

import static constants.TestConstants.*;
import static org.hamcrest.Matchers.*;

@Epic("Stellar Burgers API")
@Feature("Логин пользователя")
public class UserLoginTest extends BaseApiTest {

    private UserSteps userSteps;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        user = createTestUser();

        Response createResponse = userSteps.createUser(user);
        createResponse.then()
                .statusCode(HttpStatus.OK)
                .body(JsonKeys.SUCCESS, is(true));

        accessToken = createResponse.then()
                .extract()
                .path(JsonKeys.ACCESS_TOKEN);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
            accessToken = null;
        }
    }

    private User createTestUser() {
        return User.builder()
                .email(TestConstants.User.EMAIL_PREFIX + UUID.randomUUID() + TestConstants.User.EMAIL_DOMAIN)
                .password(TestConstants.User.DEFAULT_PASSWORD)
                .name(TestConstants.User.DEFAULT_FIRST_NAME)
                .build();
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    @Description("Проверка успешного входа с корректными данными")
    public void loginExistingUserTest() {
        Response response = userSteps.loginUser(user);

        response.then()
                .statusCode(HttpStatus.OK)
                .body(JsonKeys.SUCCESS, is(true))
                .body(JsonKeys.ACCESS_TOKEN, notNullValue())
                .body(JsonKeys.REFRESH_TOKEN, notNullValue())
                .body(JsonKeys.USER + "." + JsonKeys.EMAIL, equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Проверка ошибки при неверном пароле")
    public void loginWithInvalidPasswordTest() {
        User invalidUser = User.builder()
                .email(user.getEmail())
                .password(TestConstants.User.INCORRECT_PASSWORD)
                .name(user.getName())
                .build();

        Response response = userSteps.loginUser(invalidUser);

        response.then()
                .statusCode(HttpStatus.UNAUTHORIZED)
                .body(JsonKeys.SUCCESS, is(false))
                .body(JsonKeys.MESSAGE, equalTo(TestConstants.Error.INVALID_CREDENTIALS));
    }

    @Test
    @DisplayName("Вход с неверным email")
    @Description("Проверка ошибки при неверном email")
    public void loginWithInvalidEmailTest() {
        User invalidUser = User.builder()
                .email("nonexistent@yandex.ru")
                .password(TestConstants.User.DEFAULT_PASSWORD)
                .name(user.getName())
                .build();

        Response response = userSteps.loginUser(invalidUser);

        response.then()
                .statusCode(HttpStatus.UNAUTHORIZED)
                .body(JsonKeys.SUCCESS, is(false))
                .body(JsonKeys.MESSAGE, equalTo(TestConstants.Error.INVALID_CREDENTIALS));
    }
}