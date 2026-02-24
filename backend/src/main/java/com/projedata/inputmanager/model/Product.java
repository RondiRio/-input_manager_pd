package com.projedata.inputmanager.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// PT-BR: Entidade que representa um produto acabado que a fabrica pode fabricar.
//        Cada produto tem um preco de venda e uma lista de materias-primas necessarias
//        para produzir uma unidade. E como uma receita de bolo: voce precisa de farinha,
//        acucar e ovos, cada um na quantidade certa.
// EN-US: Entity representing a finished product the factory can manufacture.
//        Each product has a sale price and a list of raw materials needed
//        to produce one unit. It's like a cake recipe: you need flour,
//        sugar, and eggs, each in the right amount.
@Entity
@Table(name = "product")
public class Product extends PanacheEntity {

    @NotBlank(message = "Code is required")
    @Column(unique = true, nullable = false, length = 50)
    public String code;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    public String name;

    @NotNull(message = "Sale price is required")
    @DecimalMin(value = "0.01", message = "Sale price must be greater than zero")
    @Column(name = "sale_price", nullable = false, precision = 19, scale = 2)
    public BigDecimal salePrice;

    // PT-BR: A composicao e gerenciada como um agregado -- quando salvamos o produto,
    //        as composicoes vao junto. CascadeType.ALL e orphanRemoval garantem que tudo
    //        fique sincronizado, sem composicoes orfas perdidas no banco.
    // EN-US: The composition is managed as an aggregate -- when we save the product,
    //        the compositions go along with it. CascadeType.ALL and orphanRemoval ensure
    //        everything stays in sync, with no orphan compositions lost in the database.
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public List<ProductComposition> compositions = new ArrayList<>();
}
