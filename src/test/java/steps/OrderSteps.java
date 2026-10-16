package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;
import static constants.Config.ORDER_URL;
import static constants.Config.AUTH_HEADER;

public class OrderSteps {

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(Order order, String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .header(AUTH_HEADER, accessToken)
                .body(order)
                .when()
                .post(ORDER_URL);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDER_URL);
    }
}