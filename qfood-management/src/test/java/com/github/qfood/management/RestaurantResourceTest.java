package com.github.qfood.management;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.qfood.management.config.ManagementTestLifecycleManager;
import com.github.qfood.management.domain.dto.*;
import com.github.qfood.management.presentation.Paths;
import com.github.qfood.management.util.TokenUtils;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response.Status;
import java.math.BigDecimal;

import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@QuarkusTest
@QuarkusTestResource(ManagementTestLifecycleManager.class)
public class RestaurantResourceTest {

    private String token;

    @BeforeEach
    public void gereToken() throws Exception {
        token = TokenUtils.generateTokenString("/JWTOwnerClaims.json", null);
    }

    private RequestSpecification given() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .header(new Header("accept", "*/*"));
    }

    @Test
    @DataSet("restaurants-usecase-list.yml")
    public void testFindAllRestaurants() {
        String result = given()
                .when().get(Paths.RESTAURANTS)
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .asString();
        Approvals.verifyJson(result);
    }

    @Test
    public void testInsertRestaurant() {
        AddRestaurantDTO dto = new AddRestaurantDTO();
        dto.name = "Japonese Restaurant";
        dto.owner = "ID owner";
        dto.documentID = "28.193.446/0001-07";
        dto.location = new LocationDTO();
        dto.location.latitude = 999.9;
        dto.location.longitude = 999.9;
        String location = RestAssured.given()
                .body(dto)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .header(new Header("Authorization", "Bearer " + token))
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
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .asString();
        Approvals.verifyJson(result);
    }

    @Test
    @DataSet("restaurants-usecase-update.yml")
    public void testUpdateRestaurant() {
        UpdateRestaurantDTO dto = getUpdateRestaurantDTO();

        String restaurantId = "99";
        String result = given()
                .body(dto)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .pathParam("id", restaurantId)
                .when().put(Paths.RESTAURANTS + "/{id}")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .asString();

        Approvals.verifyJson(result);
    }

    @Test
    public void testUpdateRestaurantNotFound() {
        UpdateRestaurantDTO dto = getUpdateRestaurantDTO();
        String restaurantId = "55";
        given()
                .body(dto)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .pathParam("id", restaurantId)
                .when().put(Paths.RESTAURANTS + "/{id}")
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DataSet(value = "restaurants-usecase-delete.yml", strategy = SeedStrategy.REFRESH)
    public void testDeleteRestaurant() {
        String restaurantId = "55";
        given()
                .pathParam("id", restaurantId)
                .when().delete(Paths.RESTAURANTS + "/{id}")
                .then()
                .statusCode(Status.NO_CONTENT.getStatusCode());

        String result = given()
                .pathParam("id", restaurantId)
                .when().get(Paths.RESTAURANTS + "/{id}")
                .then()
                .statusCode(Status.NO_CONTENT.getStatusCode())
                .extract()
                .asString();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteRestaurantNotFound() {
        String restaurantId = "56";
        given()
                .pathParam("id", restaurantId)
                .when().delete(Paths.RESTAURANTS + "/{id}")
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DataSet(value = "restaurants-usecase-list-menus.yml", cleanAfter = true)
    public void testGetMenus() {
        String restaurantId = "123";
        String result = given()
                .pathParam("idRestaurant", restaurantId)
                .when().get(Paths.RESTAURANTS + "/{idRestaurant}/menus")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .asString();
        Approvals.verifyJson(result);
    }

    @Test
    public void testGetMenusNotFoundRestaurant() {
        String restaurantId = "123";
        given()
                .pathParam("idRestaurant", restaurantId)
                .when().get(Paths.RESTAURANTS + "/{idRestaurant}/menus")
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DataSet(value = "restaurants-usecase-add-menu.yml", strategy = SeedStrategy.INSERT)
    public void testInsertMenu() {
        AddMenuDTO dto = new AddMenuDTO();
        dto.name = "Chicken Burger";
        dto.description = "Chicken Burgar with cheese and egg";
        dto.price = new BigDecimal("14.00");

        String restaurantId = "777";
        String location = given()
                .body(dto)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .pathParam("idRestaurant", restaurantId)
                .when().post(Paths.RESTAURANTS + "/{idRestaurant}/menus")
                .then()
                .statusCode(Status.CREATED.getStatusCode())
                .extract().header("Location");

        assertTrue(location.contains(Paths.RESTAURANTS + "/" + restaurantId + "/menus"));

        // Stores the id
        String[] segments = location.split("/");
        String menuId = segments[segments.length - 1];
        assertNotNull(menuId);

        String result = given()
                .pathParam("idRestaurant", restaurantId)
                .when().get(Paths.RESTAURANTS + "/{idRestaurant}/menus")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .asString();
        assertTrue(result.contains("Chicken Burgar with cheese and egg"));
    }

    @Test
    public void testInsertMenuNotFoundRestaurant() {
        AddMenuDTO dto = new AddMenuDTO();
        dto.name = "Chicken Burger";
        dto.description = "Chicken Burgar with cheese and egg";
        dto.price = new BigDecimal("14.00");

        String restaurantId = "3432";
        given()
                .body(dto)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .pathParam("idRestaurant", restaurantId)
                .when().post(Paths.RESTAURANTS + "/{idRestaurant}/menus")
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @DataSet("restaurants-usecase-update-menu.yml")
    public void testUpdateMenu() {
        UpdateMenuDTO dto = getUpdateMenuDTO();

        String restaurantId = "444";
        String menuId = "44";
        String result = given()
                .body(dto)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .pathParam("idRestaurant", restaurantId)
                .pathParam("id", menuId)
                .when().put(Paths.RESTAURANTS + "/{idRestaurant}/menus/{id}")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .asString();

        Approvals.verifyJson(result);
    }

    @Test
    public void testUpdateMenuRestaurantNotFound() {
        UpdateMenuDTO dto = getUpdateMenuDTO();

        String restaurantId = "555";
        String menuId = "55";
        String result = given()
                .body(dto)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .pathParam("idRestaurant", restaurantId)
                .pathParam("id", menuId)
                .when().put(Paths.RESTAURANTS + "/{idRestaurant}/menus/{id}")
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode())
                .extract()
                .asString();

        assertTrue(result.isEmpty());
    }

    @Test
    @DataSet("restaurants-usecase-update-menu.yml")
    public void testUpdateMenuNotFound() {
        UpdateMenuDTO dto = getUpdateMenuDTO();

        String restaurantId = "444";
        String menuId = "45";
        String result = given()
                .body(dto)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .pathParam("idRestaurant", restaurantId)
                .pathParam("id", menuId)
                .when().put(Paths.RESTAURANTS + "/{idRestaurant}/menus/{id}")
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode())
                .extract()
                .asString();

        assertTrue(result.isEmpty());
    }

    @Test
    @DataSet("restaurants-usecase-delete-menu.yml")
    public void testDeleteMenu() {
        String restaurantId = "555";
        String menuId = "12";
        given()
                .pathParam("idRestaurant", restaurantId)
                .pathParam("id", menuId)
                .when().delete(Paths.RESTAURANTS + "/{idRestaurant}/menus/{id}")
                .then()
                .statusCode(Status.NO_CONTENT.getStatusCode());

        String result = given()
                .pathParam("idRestaurant", restaurantId)
                .when().get(Paths.RESTAURANTS + "/{idRestaurant}/menus")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .asString();

        Approvals.verifyJson(result);
    }

    @Test
    @DataSet("restaurants-usecase-delete-menu.yml")
    public void testDeleteMenuNotFound() {
        String restaurantId = "555";
        String menuId = "5";
        given()
                .pathParam("idRestaurant", restaurantId)
                .pathParam("id", menuId)
                .when().delete(Paths.RESTAURANTS + "/{idRestaurant}/menus/{id}")
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testDeleteMenuRestaurantNotFound() {
        String restaurantId = "777";
        String menuId = "12";
        given()
                .pathParam("idRestaurant", restaurantId)
                .pathParam("id", menuId)
                .when().delete(Paths.RESTAURANTS + "/{idRestaurant}/menus/{id}")
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @NotNull
    private UpdateMenuDTO getUpdateMenuDTO() {
        UpdateMenuDTO dto = new UpdateMenuDTO();
        dto.description = "Super Mini Chicken with extra bacon";
        dto.newPrice = new BigDecimal("9.99");
        return dto;
    }

    @NotNull
    private UpdateRestaurantDTO getUpdateRestaurantDTO() {
        UpdateRestaurantDTO dto = new UpdateRestaurantDTO();
        dto.newName = "Japanese Restaurant";
        dto.location = new LocationDTO();
        dto.location.latitude = 999.9;
        dto.location.longitude = 999.9;
        return dto;
    }
}
