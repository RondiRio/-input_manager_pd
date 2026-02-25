package com.projedata.inputmanager.controller;

import com.projedata.inputmanager.dto.RawMaterialDTO;
import com.projedata.inputmanager.service.RawMaterialService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

// PT-BR: Controlador REST para materias-primas. Seguindo o principio de controller fino,
//        toda a logica de negocio fica no service -- aqui so cuidamos do HTTP.
// EN-US: REST controller for raw materials. Following the thin controller principle,
//        all business logic stays in the service -- here we only handle HTTP.
@Path("/api/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class RawMaterialResource {

    @Inject
    RawMaterialService service;

    @GET
    public List<RawMaterialDTO> list() {
        return service.listAll();
    }

    @GET
    @Path("/{id}")
    public RawMaterialDTO getById(@PathParam("id") Long id) {
        return service.findById(id);
    }

    @POST
    public Response create(@Valid RawMaterialDTO dto) {
        RawMaterialDTO created = service.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public RawMaterialDTO update(@PathParam("id") Long id, @Valid RawMaterialDTO dto) {
        return service.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
