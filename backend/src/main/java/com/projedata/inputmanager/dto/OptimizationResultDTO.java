package com.projedata.inputmanager.dto;

import java.math.BigDecimal;
import java.util.List;

// PT-BR: Resultado da otimizacao de producao. Traz o plano completo: quanto produzir de
//        cada produto, o uso de cada materia-prima e a receita total maxima alcancavel.
//        E como o relatorio final que o gerente da fabrica precisa para tomar decisoes.
// EN-US: Production optimization result. Brings the full plan: how much to produce of
//        each product, the usage of each raw material, and the maximum achievable total
//        revenue. It's like the final report the factory manager needs to make decisions.
public class OptimizationResultDTO {

    public BigDecimal totalRevenue;
    public List<ProductionItem> productionPlan;
    public List<MaterialUsage> materialUsage;
    public long computationTimeMs;

    // PT-BR: Item do plano de producao -- quanto fabricar de cada produto
    // EN-US: Production plan item -- how much to manufacture of each product
    public static class ProductionItem {
        public Long productId;
        public String productName;
        public String productCode;
        public int quantityToProduce;
        public BigDecimal unitPrice;
        public BigDecimal subtotal;
    }

    // PT-BR: Uso de materia-prima -- quanto foi consumido vs. quanto estava disponivel
    // EN-US: Raw material usage -- how much was consumed vs. how much was available
    public static class MaterialUsage {
        public Long rawMaterialId;
        public String rawMaterialName;
        public BigDecimal totalAvailable;
        public BigDecimal totalUsed;
        public BigDecimal remaining;
        public String unit;
        public double usagePercentage;
    }
}
