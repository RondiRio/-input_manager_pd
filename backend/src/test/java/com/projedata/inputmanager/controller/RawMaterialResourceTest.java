package com.projedata.inputmanager.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

// PT-BR: Testes de integracao para a API REST de materias-primas.
//        Validamos os codigos HTTP, o formato das respostas e o tratamento de erros.
//        Esses testes garantem que o contrato da API esta correto, ou seja, que o
//        frontend pode confiar na estrutura das respostas.
// EN-US: Integration tests for the raw materials REST API.
//        We validate HTTP codes, response format, and error handling.
//        These tests ensure the API contract is correct, meaning the frontend
//        can trust the response structure.
@QuarkusTest
class RawMaterialResourceTest {

    @Test
    void testListAll_returnsOk() {
        given()
            .when().get("/api/raw-materials")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void testListAll_returnsSeedData() {
        // PT-BR: O import.sql carrega 5 materias-primas iniciais
        // EN-US: The import.sql loads 5 initial raw materials
        given()
            .when().get("/api/raw-materials")
            .then()
            .statusCode(200)
            .body("$.size()", greaterThanOrEqualTo(1));
    }

    @Test
    void testCreate_validMaterial_returns201() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"code\":\"TEST001\",\"name\":\"Test Material\",\"stockQuantity\":100.5,\"unit\":\"g\"}")
            .when().post("/api/raw-materials")
            .then()
            .statusCode(201)
            .body("code", equalTo("TEST001"))
            .body("name", equalTo("Test Material"))
            .body("id", notNullValue());
    }

    @Test
    void testCreate_missingName_returns400() {
        // PT-BR: Requisicao sem nome deve retornar erro de validacao
        // EN-US: Request without name should return validation error
        given()
            .contentType(ContentType.JSON)
            .body("{\"code\":\"TEST002\",\"stockQuantity\":100,\"unit\":\"g\"}")
            .when().post("/api/raw-materials")
            .then()
            .statusCode(400);
    }

    @Test
    void testCreate_negativeStock_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"code\":\"TEST003\",\"name\":\"Negative\",\"stockQuantity\":-5,\"unit\":\"g\"}")
            .when().post("/api/raw-materials")
            .then()
            .statusCode(400);
    }

    @Test
    void testGetById_nonExistent_returns404() {
        given()
            .when().get("/api/raw-materials/99999")
            .then()
            .statusCode(404);
    }

    @Test
    void testDelete_nonExistent_returns404() {
        given()
            .when().delete("/api/raw-materials/99999")
            .then()
            .statusCode(404);
    }
}
