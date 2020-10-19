package com.hotjoe.services.tests;

import com.google.gson.Gson;
import com.hotjoe.services.model.Product;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class ProductServiceTests {

    @Test
    public void testHeartbeat() {
        given()
          .when().get("/v1/heartbeat")
          .then()
             .statusCode(200)
                .and()
             .body(is("OK"));
    }

    @Test
    public void testHeartbeatBadMethod() {
        given()
                .when().post("/v1/heartbeat")
                .then()
                .statusCode(405);
    }

    @Test
    public void testNewProductHappyPath() {
        Random random = new Random();
        Integer productId = random.nextInt(100000);

        Product product = new Product();
        product.setProductId(productId);
        product.setDescription("This is the product description");
        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        product.setCreateDate(formatter.format(now));

        given().header("Content-Type", "application/json")
                .when()
                .body(new Gson().toJson(product))
                .post("/v1/product")
                .then()
                .statusCode(201)
                .body("description", equalTo(product.getDescription()))
                .body("productId", equalTo(product.getProductId()));
    }

    @Test
    public void testNoProduct() {

        given().header("Content-Type", "application/json")
                .when()
                .post("/v1/product")
                .then()
                .statusCode(400);
    }

    @Test
    public void testNoProductId()  {
        Product product = new Product();
        product.setDescription("This is the product description");
        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        product.setCreateDate(formatter.format(now));

        given().header("Content-Type", "application/json")
                .when()
                .body(new Gson().toJson(product))
                .post("/v1/product")
                .then()
                .statusCode(201)
                .body("description", equalTo(product.getDescription()))
                .body("$", hasKey("productId"));
    }

    @Test
    public void testNewProductDuplicateId() {
        Random random = new Random();
        Integer productId = random.nextInt(100000);

        Product product = new Product();
        product.setProductId(productId);
        product.setDescription("This is the product description");
        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        product.setCreateDate(formatter.format(now));

        given().header("Content-Type", "application/json")
                .when()
                .body(new Gson().toJson(product))
                .post("/v1/product")
                .then()
                .statusCode(201)
                .body("description", equalTo(product.getDescription()))
                .body("productId", equalTo(product.getProductId()));

        given().header("Content-Type", "application/json")
                .when()
                .body(new Gson().toJson(product))
                .post("/v1/product")
                .then()
                .statusCode(409);
    }

    @Test
    public void testNewProductBadDescription() {
        Random random = new Random();
        Integer productId = random.nextInt(100000);

        Product product = new Product();
        product.setProductId(productId);
        product.setDescription("This is the product-description");
        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        product.setCreateDate(formatter.format(now));

        given().header("Content-Type", "application/json")
                .when()
                .body(new Gson().toJson(product))
                .post("/v1/product")
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetProduct() {
        Random random = new Random();
        Integer productId = random.nextInt(100000);

        Product product = new Product();
        product.setProductId(productId);
        product.setDescription("This is the product description");
        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        product.setCreateDate(formatter.format(now));

        given().header("Content-Type", "application/json")
                .when()
                .body(new Gson().toJson(product))
                .post("/v1/product")
                .then()
                .statusCode(201)
                .body("description", equalTo(product.getDescription()))
                .body("productId", equalTo(product.getProductId()));

        given()
                .when()
                .get( "/v1/product/" + productId)
                .then()
                .statusCode(200)
                .body("productId", equalTo(productId) );
    }

    @Test
    public void testGetBadProductId() {
        given()
                .when()
                .get( "/v1/product/1234")
                .then()
                .statusCode(404);
    }
}