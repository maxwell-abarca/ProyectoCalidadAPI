package com.project.demo.rest.category;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {

    @LocalServerPort
    int port;

    String token;
    Long categoryId;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        token = given()
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

        categoryId = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/categories")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract()
                .jsonPath()
                .getLong("[0].id");
    }


    @Test
    void shouldReturnCategoryByExistingId() {
        given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", categoryId)
                .when()
                .get("/categories/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(categoryId.intValue()));
    }


    @Test
    void shouldReturnCategoryNameWhenIdExists() {
        given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", categoryId)
                .when()
                .get("/categories/{id}")
                .then()
                .statusCode(200)
                .body("name", not(equalTo("")));
    }


    @Test
    void shouldReturnCategoryDescriptionWhenIdExists() {
        given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", categoryId)
                .when()
                .get("/categories/{id}")
                .then()
                .statusCode(200)
                .body("description", not(equalTo("")));
    }


    @Test
    void shouldReturnCategoryImageWhenIdExists() {
        given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", categoryId)
                .when()
                .get("/categories/{id}")
                .then()
                .statusCode(200)
                .body("image", not(equalTo("")));
    }


    @Test
    void shouldReturn500WhenCategoryIdDoesNotExist() {
        long nonExistingId = 999999L;

        given()
                .header("Authorization", "Bearer " + token)
                .pathParam("id", nonExistingId)
                .when()
                .get("/categories/{id}")
                .then()
                .statusCode(500)
                .body("status", equalTo(500))
                .body("description", equalTo("Unknown internal server error."));
    }


    @Test
    void shouldReturn500WhenCategoryIdIsNotNumeric() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/categories/abc")
                .then()
                .statusCode(500)
                .body("status", equalTo(500))
                .body("description", equalTo("Unknown internal server error."));
    }

    @Test
    void getCategories_tokenValido() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/categories")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    void getCategories_sinToken() {

        given()
                .when()
                .get("/categories")
                .then()
                .statusCode(403);
    }
}