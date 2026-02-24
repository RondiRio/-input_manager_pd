package com.projedata.inputmanager.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

// PT-BR: Controlador de autenticacao. Para o MVP, usamos uma validacao simples com
//        usuario e senha fixos. Em producao, isso seria substituido por uma integracao
//        com LDAP, banco de dados ou provedor OAuth. A estrutura do JWT ja esta
//        preparada para quando a seguranca for ativada via SmallRye JWT.
// EN-US: Authentication controller. For the MVP, we use simple validation with fixed
//        username and password. In production, this would be replaced with an LDAP,
//        database, or OAuth provider integration. The JWT structure is already prepared
//        for when security is enabled via SmallRye JWT.
@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    // PT-BR: Credenciais MVP. Em producao, nunca hardcode credenciais no codigo.
    //        Use variaveis de ambiente ou um servico de secrets.
    // EN-US: MVP credentials. In production, never hardcode credentials in code.
    //        Use environment variables or a secrets service.
    private static final String MVP_USERNAME = "admin";
    private static final String MVP_PASSWORD = "admin123";

    @POST
    @Path("/login")
    @PermitAll
    public Response login(Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (MVP_USERNAME.equals(username) && MVP_PASSWORD.equals(password)) {
            // PT-BR: Em producao, aqui gerariamos um token JWT real com SmallRye JWT Build.
            //        Por enquanto, retornamos um token placeholder para validar o fluxo.
            // EN-US: In production, we would generate a real JWT token with SmallRye JWT Build.
            //        For now, we return a placeholder token to validate the flow.
            return Response.ok(Map.of(
                "token", "mvp-jwt-placeholder-token",
                "username", username,
                "role", "admin"
            )).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED)
            .entity(Map.of("error", "Invalid credentials"))
            .build();
    }
}
