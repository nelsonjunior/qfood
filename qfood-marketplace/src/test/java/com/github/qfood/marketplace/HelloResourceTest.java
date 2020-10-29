package com.github.qfood.marketplace;

import com.github.qfood.marketplace.presentation.Paths;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class HelloResourceTest {

    @Test
    public void testMenuFindAllEndpoint() {
        String body = given()
                .when().get(Paths.MENUS)
                .then()
                .statusCode(200)
                .extract().asString();
        System.out.println(body);
    }
}