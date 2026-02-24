package com.projedata.inputmanager.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

// PT-BR: DTO para a composicao de um produto. Na requisicao, o frontend envia o ID
//        da materia-prima e a quantidade. Na resposta, incluimos tambem o nome e a
//        unidade da materia-prima para facilitar a exibicao sem precisar de outra chamada.
// EN-US: DTO for product composition. In the request, the frontend sends the raw material
//        ID and quantity. In the response, we also include the raw material name and unit
//        to make display easier without needing another API call.
public class ProductCompositionDTO {

    public Long id;

    @NotNull(message = "Raw material ID is required")
    public Long rawMaterialId;

    // PT-BR: Campos somente-leitura, populados na resposta
    // EN-US: Read-only fields, populated in the response
    public String rawMaterialName;
    public String rawMaterialUnit;

    @NotNull(message = "Required quantity is required")
    @DecimalMin(value = "0.0001", message = "Required quantity must be greater than zero")
    public BigDecimal requiredQuantity;
}
