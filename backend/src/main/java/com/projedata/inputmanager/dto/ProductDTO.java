package com.projedata.inputmanager.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

// PT-BR: DTO para produto. Inclui a lista de composicoes embutida para que o frontend
//        possa criar/editar um produto e sua receita em uma unica chamada de API.
//        A anotacao @Valid garante que cada composicao na lista tambem sera validada.
// EN-US: DTO for product. Includes the embedded composition list so the frontend can
//        create/edit a product and its recipe in a single API call.
//        The @Valid annotation ensures each composition in the list is also validated.
public class ProductDTO {

    public Long id;

    @NotBlank(message = "Code is required")
    public String code;

    @NotBlank(message = "Name is required")
    public String name;

    @NotNull(message = "Sale price is required")
    @DecimalMin(value = "0.01", message = "Sale price must be greater than zero")
    public BigDecimal salePrice;

    @Valid
    public List<ProductCompositionDTO> compositions;
}
