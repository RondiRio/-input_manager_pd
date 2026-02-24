package com.projedata.inputmanager.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

// PT-BR: Entidade que representa uma materia-prima (insumo) no estoque da fabrica.
//        Cada insumo tem um codigo unico, nome, quantidade disponivel e unidade de medida.
//        Essa e a base de tudo -- sem materias-primas, nao ha producao.
// EN-US: Entity representing a raw material (input) in the factory's inventory.
//        Each input has a unique code, name, available quantity, and unit of measure.
//        This is the foundation of everything -- without raw materials, there is no production.
@Entity
@Table(name = "raw_material")
public class RawMaterial extends PanacheEntity {

    @NotBlank(message = "Code is required")
    @Column(unique = true, nullable = false, length = 50)
    public String code;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    public String name;

    // PT-BR: Usamos BigDecimal porque precisao importa quando lidamos com gramas,
    //        mililitros e quantidades fracionarias. Ninguem quer perder 0.001g de ouro
    //        por causa de um arredondamento de ponto flutuante.
    // EN-US: We use BigDecimal because precision matters when dealing with grams,
    //        milliliters, and fractional quantities. Nobody wants to lose 0.001g of gold
    //        due to floating-point rounding.
    @NotNull(message = "Stock quantity is required")
    @DecimalMin(value = "0.0", message = "Stock quantity must be zero or positive")
    @Column(name = "stock_quantity", nullable = false, precision = 19, scale = 4)
    public BigDecimal stockQuantity;

    @NotBlank(message = "Unit is required")
    @Column(nullable = false, length = 20)
    public String unit;
}
