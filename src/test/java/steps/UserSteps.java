package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.User;

import static io.restassured.RestAssured.given;
import static constants.Config.*;
import static constants.Config.AUTH_HEADER;

public class UserSteps {

    @Step("Создание пользователя: {user.email}")
    public Response createUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(REGISTER_URL);
    }

    @Step("Логин пользователя: {user.email}")
    public Response loginUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(LOGIN_URL);
    }

    @Step("Удаление пользователя по токену")
    public void deleteUser(String accessToken) {
        given()
                .header(AUTH_HEADER, accessToken)
                .when()
                .delete(USER_URL);
    }
}
