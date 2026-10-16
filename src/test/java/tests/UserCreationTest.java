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
@Feature("Создание пользователя")
public class UserCreationTest extends BaseApiTest {

    private UserSteps userSteps;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        user = createTestUser();
        accessToken = null;
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
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешного создания нового пользователя")
    public void createUniqueUserTest() {
        Response response = userSteps.createUser(user);
        accessToken = response.then().extract().path(JsonKeys.ACCESS_TOKEN);

        response.then()
                .statusCode(HttpStatus.OK)
                .body(JsonKeys.SUCCESS, is(true))
                .body(JsonKeys.USER + "." + JsonKeys.EMAIL, equalTo(user.getEmail()))
                .body(JsonKeys.USER + "." + JsonKeys.NAME, equalTo(user.getName()))
                .body(JsonKeys.ACCESS_TOKEN, notNullValue())
                .body(JsonKeys.REFRESH_TOKEN, notNullValue());
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    @Description("Проверка ошибки при попытке создания дублирующего пользователя")
    public void createDuplicateUserTest() {
        Response firstResponse = userSteps.createUser(user);
        accessToken = firstResponse.then().extract().path(JsonKeys.ACCESS_TOKEN);

        Response secondResponse = userSteps.createUser(user);

        secondResponse.then()
                .statusCode(HttpStatus.FORBIDDEN)
                .body(JsonKeys.SUCCESS, is(false))
                .body(JsonKeys.MESSAGE, equalTo(TestConstants.Error.USER_ALREADY_EXISTS));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля (email)")
    @Description("Проверка ошибки при создании пользователя без поля email")
    public void createUserWithoutEmailTest() {
        user.setEmail(null);
        Response response = userSteps.createUser(user);

        response.then()
                .statusCode(HttpStatus.FORBIDDEN)
                .body(JsonKeys.SUCCESS, is(false))
                .body(JsonKeys.MESSAGE, equalTo(TestConstants.Error.REQUIRED_FIELDS));
    }
}