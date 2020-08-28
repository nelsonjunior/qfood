package com.github.qfood.management;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.qfood.management.config.CadastroTestLifecycleManager;
import com.github.qfood.management.domain.entity.Restaurant;
import com.github.qfood.management.presentation.Paths;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response.Status;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@QuarkusTest
@QuarkusTestResource(CadastroTestLifecycleManager.class)
public class RestaurantResourceTest {

    @Test
    @DataSet("restaurants-usecase-1.yml")
    public void testFindAllRestaurants() {
        String result = given()
                .when().get(Paths.RESTAURANTS)
                .then()
                .statusCode(200)
                .extract()
                .asString();
        Approvals.verifyJson(result);
    }

    @Test
    public void testInsertRestaurant() {
        Restaurant dto = new Restaurant();
        dto.name = "Japonese Restaurant";
        dto.owner = "ID owner";
        String location = given()
                .body(dto)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .when().post(Paths.RESTAURANTS)
                .then()
                .statusCode(Status.CREATED.getStatusCode())
                .extract().header("Location");

        assertTrue(location.contains(Paths.RESTAURANTS));

        // Stores the id
        String[] segments = location.split("/");
        String restaurantId = segments[segments.length - 1];
        assertNotNull(restaurantId);

        String result = given()
                .pathParam("id", restaurantId)
                .when().get(Paths.RESTAURANTS + "/{id}")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertNotNull(result);

    }
}
