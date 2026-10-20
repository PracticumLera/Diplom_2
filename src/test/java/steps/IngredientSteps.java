package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static constants.Config.INGREDIENT_URL;

public class IngredientSteps {

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return given()
                .when()
                .get(INGREDIENT_URL);
    }

    @Step("Получение первых {count} ID ингредиентов")
    public String[] getFirstIngredientsIds(int count) {
        List<String> allIds = getIngredients()
                .then()
                .extract()
                .path("data._id");

        return allIds.stream()
                .limit(count)
                .toArray(String[]::new);
    }
}