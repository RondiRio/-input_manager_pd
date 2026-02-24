package com.projedata.inputmanager.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// PT-BR: Handler global de excecoes que traduz excecoes Java em respostas HTTP amigaveis.
//        Em vez de deixar o usuario ver um stack trace assustador, devolvemos mensagens
//        claras e codigos HTTP corretos. E como um tradutor entre o mundo tecnico e o
//        mundo do usuario final.
// EN-US: Global exception handler that translates Java exceptions into user-friendly
//        HTTP responses. Instead of letting the user see a scary stack trace, we return
//        clear messages and correct HTTP codes. It's like a translator between the
//        technical world and the end-user world.
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {

        if (exception instanceof ResourceNotFoundException) {
            return buildResponse(Response.Status.NOT_FOUND, exception.getMessage());
        }

        if (exception instanceof ConstraintViolationException cve) {
            return handleConstraintViolation(cve);
        }

        if (exception instanceof IllegalArgumentException) {
            return buildResponse(Response.Status.BAD_REQUEST, exception.getMessage());
        }

        // PT-BR: Para excecoes inesperadas, logamos o erro completo e devolvemos uma
        //        mensagem generica. Nunca exponha detalhes internos ao usuario.
        // EN-US: For unexpected exceptions, we log the full error and return a generic
        //        message. Never expose internal details to the user.
        return buildResponse(
            Response.Status.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred. Please try again later."
        );
    }

    private Response handleConstraintViolation(ConstraintViolationException cve) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Validation failed");
        body.put("violations", cve.getConstraintViolations().stream()
            .map(this::formatViolation)
            .collect(Collectors.toList()));
        return Response.status(Response.Status.BAD_REQUEST)
            .type(MediaType.APPLICATION_JSON)
            .entity(body)
            .build();
    }

    private Map<String, String> formatViolation(ConstraintViolation<?> violation) {
        Map<String, String> detail = new HashMap<>();
        String field = violation.getPropertyPath().toString();
        // PT-BR: Extraimos apenas o nome do campo, sem o prefixo do metodo
        // EN-US: Extract only the field name, without the method prefix
        if (field.contains(".")) {
            field = field.substring(field.lastIndexOf('.') + 1);
        }
        detail.put("field", field);
        detail.put("message", violation.getMessage());
        return detail;
    }

    private Response buildResponse(Response.Status status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", message);
        body.put("status", status.getStatusCode());
        return Response.status(status)
            .type(MediaType.APPLICATION_JSON)
            .entity(body)
            .build();
    }
}
