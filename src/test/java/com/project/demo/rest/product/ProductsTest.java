package com.project.demo.rest.product;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductsTest {

    @LocalServerPort
    int port;

    private String buyerToken;
    private String adminToken;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        //credenciales de UserSeeder- el admin es de un User existente para no modificar el backend
        buyerToken = obtenerToken("user.buyer@gmail.com", "userbuyer123");
        adminToken = obtenerToken("mg@gmail.com", "manuel123");
    }

    private String obtenerToken(String email, String password) {
        return given()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
    }

    // --- PRUEBA 1 --- debe ser OK
    @Test
    @DisplayName("1. GET/products - Token válido de Buyer")
    void shouldReturnProductsListForValidBuyer() {
        given()
                .header("Authorization", "Bearer " + buyerToken)
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", is(instanceOf(java.util.List.class))) // Verifica que es una lista
                .body("size()", greaterThan(0)) // Verifica que no está vacía
                .body("name", hasItem("Camisa")); // Verifica dato real del Seeder
    }

    // --- PRUEBA 2 --- debe ser OK
    @Test
    @DisplayName("2. GET/products - Token válido de Super Admin")
    void shouldReturnProductsListForValidAdmin() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("name", hasItem("Sweater de mujer"));
    }

    // --- PRUEBA 3 --- debe ser OK
    @Test
    @DisplayName("3. GET/products - Sin Token (Acceso Denegado)")
    void shouldReturn403WhenNoTokenProvided() {
        given()
                .when()
                .get("/products")
                .then()
                .statusCode(403);
    }

    // --- PRUEBA 4 --- debe ser negativa
    @Test
    @DisplayName("4. GET/products - Token Inválido")
    void shouldReturn403WhenTokenIsInvalid() {
        given()
                .header("Authorization", "Bearer token_falso_123")
                .when()
                .get("/products")
                .then()
                .statusCode(403);
    }

    // --- PRUEBA 5 --- debe ser OK
    @Test
    @DisplayName("5. GET/products/{id} - ID Existente")
    void shouldReturnSpecificProductWhenIdExists() {
        Long existingId = 1L;
        given()
                .header("Authorization", "Bearer " + buyerToken)
                .pathParam("id", existingId)
                .when()
                .get("/products/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(existingId.intValue()))
                .body("name", notNullValue())
                .body("price", notNullValue());
    }

    // --- PRUEBA 6 --- debe ser OK
    @Test
    @DisplayName("6. GET/products/{id} - ID Inexistente")
    void shouldReturn500WhenProductIdDoesNotExist() {
        // El controlador usa .orElseThrow(RuntimeException::new), lo que causa un 500
        given()
                .header("Authorization", "Bearer " + buyerToken)
                .pathParam("id", 999999999)
                .when()
                .get("/products/{id}")
                .then()
                .statusCode(500);
    }

    // --- PRUEBA 7 --- debe ser negativa
    @Test
    @DisplayName("7. GET/products/{id} - ID No Numérico")
    void shouldReturn400WhenIdIsNotNumeric() {
        given()
                .header("Authorization", "Bearer " + buyerToken)
                .pathParam("id", "abc")
                .when()
                .get("/products/{id}")
                .then()
                .statusCode(400); // Spring Boot falla en la conversión de Long
    }

    @Test
    void filterByName_nombreExistente() {

        given()
                .header("Authorization", "Bearer " + buyerToken)
                .pathParam("name", "Camisa")
                .when()
                .get("/products/filterByName/{name}")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    void filterByName_coincidenciaParcial() {

        given()
                .header("Authorization", "Bearer " + buyerToken)
                .pathParam("name", "Cam")
                .when()
                .get("/products/filterByName/{name}")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    void filterByName_nombreInexistente() {

        given()
                .header("Authorization", "Bearer " + buyerToken)
                .pathParam("name", "productoNoExistente")
                .when()
                .get("/products/filterByName/{name}")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }

    @Test
    void filterByName_tokenInvalido() {

        given()
                .header("Authorization", "Bearer tokenInvalido")
                .pathParam("name", "Camisa")
                .when()
                .get("/products/filterByName/{name}")
                .then()
                .statusCode(403);
    }

}