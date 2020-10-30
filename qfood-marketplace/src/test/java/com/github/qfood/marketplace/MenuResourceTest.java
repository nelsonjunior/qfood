package com.github.qfood.marketplace;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class MenuResourceTest {

    @Test
    public void testMenuFindAllEndpoint() {
        String body = given()
                .when().get("/menus")
                .then()
                .statusCode(200)
                .extract().asString();
        System.out.println(body);
    }
}