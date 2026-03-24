package com.project.demo.rest.product;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @LocalServerPort
    int port;

    String token;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        token = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                      "email": "user.buyer@gmail.com",
                      "password": "userbuyer123"
                    }
                """)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .jsonPath()
                .getString("token");
    }

    @Test
    void shouldReturnProductsWhenTokenIsValid() {
        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/products")
                .then()
                .statusCode(200);
    }
}