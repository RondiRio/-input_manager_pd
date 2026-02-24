package com.projedata.inputmanager.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

// PT-BR: Tabela de juncao que conecta produtos a materias-primas, definindo quanto de
//        cada insumo e necessario para fabricar uma unidade do produto. Pense nisso como
//        a linha de uma receita: "300g de farinha de trigo para 1 bolo".
//        A restricao unica garante que nao teremos a mesma materia-prima duplicada
//        na composicao de um mesmo produto.
// EN-US: Join table connecting products to raw materials, defining how much of each
//        input is needed to manufacture one unit of the product. Think of it as a
//        recipe line: "300g of wheat flour for 1 cake".
//        The unique constraint ensures we won't have the same raw material duplicated
//        in the composition of a single product.
@Entity
@Table(name = "product_composition",
       uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "raw_material_id"}))
public class ProductComposition extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "Product is required")
    public Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "raw_material_id", nullable = false)
    @NotNull(message = "Raw material is required")
    public RawMaterial rawMaterial;

    @NotNull(message = "Required quantity is required")
    @DecimalMin(value = "0.0001", message = "Required quantity must be greater than zero")
    @Column(name = "required_quantity", nullable = false, precision = 19, scale = 4)
    public BigDecimal requiredQuantity;
}
