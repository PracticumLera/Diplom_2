package tests;

import base.BaseApiTest;
import constants.TestConstants;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.IngredientSteps;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.UUID;

import static constants.TestConstants.*;
import static org.hamcrest.Matchers.*;

@Epic("Stellar Burgers API")
@Feature("Создание заказа")
public class OrderCreationTest extends BaseApiTest {

    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();
    private final IngredientSteps ingredientSteps = new IngredientSteps();

    private String accessToken;
    private String[] validIngredientIds;

    @Before
    public void setUp() {
        User user = User.builder()
                .email(TestConstants.User.EMAIL_PREFIX + UUID.randomUUID() + TestConstants.User.EMAIL_DOMAIN)
                .password(TestConstants.User.DEFAULT_PASSWORD)
                .name(TestConstants.User.DEFAULT_FIRST_NAME)
                .build();

        accessToken = userSteps.createUser(user)
                .then()
                .statusCode(HttpStatus.OK)
                .extract()
                .path(JsonKeys.ACCESS_TOKEN);

        validIngredientIds = ingredientSteps.getFirstIngredientsIds(2);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
            accessToken = null;
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Успешное создание заказа авторизованным пользователем с ингредиентами")
    public void createOrderWithAuthAndIngredientsTest() {
        Order order = Order.builder()
                .ingredients(validIngredientIds)
                .build();

        Response response = orderSteps.createOrderWithAuth(order, accessToken);

        response.then()
                .statusCode(HttpStatus.OK)
                .body(JsonKeys.SUCCESS, is(true))
                .body(JsonKeys.ORDER_NUMBER, notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка создания заказа неавторизованным пользователем")
    public void createOrderWithoutAuthTest() {
        Order order = Order.builder()
                .ingredients(validIngredientIds)
                .build();

        Response response = orderSteps.createOrderWithoutAuth(order);

        response.then()
                .statusCode(HttpStatus.OK)
                .body(JsonKeys.SUCCESS, is(true))
                .body(JsonKeys.NAME, notNullValue())
                .body(JsonKeys.ORDER_NUMBER, notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка ошибки при создании заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Order order = Order.builder()
                .ingredients(new String[0])
                .build();

        Response response = orderSteps.createOrderWithAuth(order, accessToken);

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST)
                .body(JsonKeys.SUCCESS, is(false))
                .body(JsonKeys.MESSAGE, equalTo(TestConstants.Error.INGREDIENTS_REQUIRED));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов (ожидаем 400, придёт 500)")
    @Description("По заданию тест должен падать — сервер возвращает 500 вместо 400")
    public void createOrderWithInvalidHashTest() {
        Order order = Order.builder()
                .ingredients(new String[]{Ingredient.INVALID_HASH})
                .build();

        Response response = orderSteps.createOrderWithAuth(order, accessToken);

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание заказа с несуществующим ID ингредиента")
    @Description("По заданию тест должен падать — сервер возвращает 500 вместо 400")
    public void createOrderWithNonExistentIdTest() {
        Order order = Order.builder()
                .ingredients(new String[]{Ingredient.NON_EXISTENT_ID})
                .build();

        Response response = orderSteps.createOrderWithAuth(order, accessToken);

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST);
    }
}