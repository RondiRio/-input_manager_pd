package com.projedata.inputmanager.controller;

import com.projedata.inputmanager.dto.OptimizationResultDTO;
import com.projedata.inputmanager.service.ProductionOptimizationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

// PT-BR: Controlador REST para a otimizacao de producao. Recebe um POST e retorna
//        o plano otimo de producao baseado no estoque atual de materias-primas.
//        E um endpoint simples porque toda a complexidade esta no service.
// EN-US: REST controller for production optimization. Receives a POST and returns
//        the optimal production plan based on the current raw material stock.
//        It's a simple endpoint because all the complexity lives in the service.
@Path("/api/optimization")
@Produces(MediaType.APPLICATION_JSON)
public class ProductionOptimizationResource {

    @Inject
    ProductionOptimizationService service;

    @POST
    @Path("/optimize")
    public OptimizationResultDTO optimize() {
        return service.optimize();
    }
}
