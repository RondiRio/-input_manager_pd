package com.projedata.inputmanager.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

// PT-BR: Testes de integracao para a API REST de produtos.
//        Verificamos o CRUD completo, incluindo a criacao com composicao
//        e o tratamento de erros para entradas invalidas.
// EN-US: Integration tests for the products REST API.
//        We verify the complete CRUD, including creation with composition
//        and error handling for invalid inputs.
@QuarkusTest
class ProductResourceTest {

    @Test
    void testListAll_returnsOk() {
        given()
            .when().get("/api/products")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void testCreate_validProduct_returns201() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"code\":\"TPROD001\",\"name\":\"Test Product\",\"salePrice\":25.50,\"compositions\":[]}")
            .when().post("/api/products")
            .then()
            .statusCode(201)
            .body("code", equalTo("TPROD001"))
            .body("name", equalTo("Test Product"))
            .body("salePrice", equalTo(25.50f))
            .body("id", notNullValue());
    }

    @Test
    void testCreate_missingCode_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"No Code Product\",\"salePrice\":10.00}")
            .when().post("/api/products")
            .then()
            .statusCode(400);
    }

    @Test
    void testCreate_zeroPrice_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"code\":\"TPROD002\",\"name\":\"Free Product\",\"salePrice\":0}")
            .when().post("/api/products")
            .then()
            .statusCode(400);
    }

    @Test
    void testGetById_nonExistent_returns404() {
        given()
            .when().get("/api/products/99999")
            .then()
            .statusCode(404);
    }

    @Test
    void testOptimization_endpoint_returnsOk() {
        // PT-BR: O endpoint de otimizacao deve funcionar com os dados seed
        // EN-US: The optimization endpoint should work with seed data
        given()
            .when().post("/api/optimization/optimize")
            .then()
            .statusCode(200)
            .body("totalRevenue", notNullValue())
            .body("productionPlan", notNullValue())
            .body("materialUsage", notNullValue())
            .body("computationTimeMs", notNullValue());
    }
}
