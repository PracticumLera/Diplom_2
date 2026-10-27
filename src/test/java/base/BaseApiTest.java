package base;

import constants.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class BaseApiTest {

    @BeforeClass
    public static void setUpBase() {
        RestAssured.baseURI = Config.BASE_URL;
        RestAssured.basePath = "";

        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @AfterClass
    public static void tearDownBase() {
        RestAssured.reset();
    }
}