package com.projedata.inputmanager.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

// PT-BR: Objeto de transferencia de dados para materia-prima. Desacopla a API da entidade
//        JPA, evitando expor detalhes internos do banco e problemas de serializacao circular.
// EN-US: Data transfer object for raw material. Decouples the API from the JPA entity,
//        avoiding exposure of internal database details and circular serialization issues.
public class RawMaterialDTO {

    public Long id;

    @NotBlank(message = "Code is required")
    public String code;

    @NotBlank(message = "Name is required")
    public String name;

    @NotNull(message = "Stock quantity is required")
    @DecimalMin(value = "0.0", message = "Stock quantity must be zero or positive")
    public BigDecimal stockQuantity;

    @NotBlank(message = "Unit is required")
    public String unit;
}
