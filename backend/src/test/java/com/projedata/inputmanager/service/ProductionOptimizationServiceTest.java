package com.projedata.inputmanager.service;

import com.projedata.inputmanager.dto.OptimizationResultDTO;
import com.projedata.inputmanager.model.Product;
import com.projedata.inputmanager.model.ProductComposition;
import com.projedata.inputmanager.model.RawMaterial;
import com.projedata.inputmanager.repository.ProductRepository;
import com.projedata.inputmanager.repository.RawMaterialRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// PT-BR: Testes do algoritmo de otimizacao de producao. Estes testes sao os mais
//        importantes do sistema porque validam a logica central do negocio.
//        Cobrimos desde cenarios simples (um produto, um insumo) ate situacoes
//        complexas (produtos competindo por materias-primas, estoque zerado, etc.)
//        Pense nesses testes como uma rede de seguranca: se alguem alterar o algoritmo
//        no futuro, os testes vao dizer imediatamente se algo quebrou.
// EN-US: Tests for the production optimization algorithm. These are the most important
//        tests in the system because they validate the core business logic.
//        We cover from simple scenarios (one product, one input) to complex situations
//        (products competing for raw materials, zero stock, etc.)
//        Think of these tests as a safety net: if someone changes the algorithm in the
//        future, the tests will immediately tell if something broke.
@QuarkusTest
class ProductionOptimizationServiceTest {

    @Inject
    ProductionOptimizationService service;

    @Inject
    RawMaterialRepository rawMaterialRepository;

    @Inject
    ProductRepository productRepository;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        // PT-BR: Limpamos o banco antes de cada teste para garantir isolamento total.
        //        Cada teste comeca com uma base limpa, sem influencia dos anteriores.
        // EN-US: We clean the database before each test to ensure total isolation.
        //        Each test starts with a clean slate, with no influence from previous ones.
        productRepository.deleteAll();
        rawMaterialRepository.deleteAll();
    }

    @Test
    @Transactional
    void testOptimization_singleProduct_sufficientStock() {
        // PT-BR: Caso mais simples: um produto, recursos suficientes para 5 unidades.
        //        Se o algoritmo nao acertar esse, temos um problema serio.
        // EN-US: Simplest case: one product, resources sufficient for 5 units.
        //        If the algorithm doesn't get this right, we have a serious problem.
        RawMaterial flour = createMaterial("MP001", "Flour", 500, "g");
        Product cake = createProduct("PROD001", "Cake", 45.00);
        addComposition(cake, flour, 100);

        OptimizationResultDTO result = service.optimize();

        assertNotNull(result);
        assertEquals(0, new BigDecimal("225.00").compareTo(result.totalRevenue));
        assertEquals(1, result.productionPlan.size());
        assertEquals(5, result.productionPlan.get(0).quantityToProduce);
    }

    @Test
    @Transactional
    void testOptimization_competingProducts_choosesHigherValue() {
        // PT-BR: Dois produtos competindo pela mesma materia-prima.
        //        O algoritmo deve encontrar a combinacao que maximiza o valor total.
        //        Com 500g de farinha: Produto A (R$50, usa 200g) vs Produto B (R$30, usa 100g).
        //        Opcao 1: 2A + 0B = R$100. Opcao 2: 0A + 5B = R$150. Opcao 3: 1A + 3B = R$140.
        //        O otimo e 5 unidades de B = R$150.
        // EN-US: Two products competing for the same raw material.
        //        The algorithm must find the combination that maximizes total value.
        //        With 500g flour: Product A ($50, uses 200g) vs Product B ($30, uses 100g).
        //        Option 1: 2A + 0B = $100. Option 2: 0A + 5B = $150. Option 3: 1A + 3B = $140.
        //        The optimum is 5 units of B = $150.
        RawMaterial flour = createMaterial("MP001", "Flour", 500, "g");

        Product productA = createProduct("PROD001", "Product A", 50.00);
        addComposition(productA, flour, 200);

        Product productB = createProduct("PROD002", "Product B", 30.00);
        addComposition(productB, flour, 100);

        OptimizationResultDTO result = service.optimize();

        assertNotNull(result);
        assertEquals(0, new BigDecimal("150.00").compareTo(result.totalRevenue));
    }

    @Test
    @Transactional
    void testOptimization_noStock() {
        // PT-BR: Sem estoque, nao ha producao. Receita deve ser zero.
        // EN-US: Without stock, there is no production. Revenue must be zero.
        RawMaterial flour = createMaterial("MP001", "Flour", 0, "g");
        Product cake = createProduct("PROD001", "Cake", 45.00);
        addComposition(cake, flour, 100);

        OptimizationResultDTO result = service.optimize();

        assertNotNull(result);
        assertEquals(0, BigDecimal.ZERO.compareTo(result.totalRevenue));
        assertTrue(result.productionPlan.stream().allMatch(p -> p.quantityToProduce == 0));
    }

    @Test
    @Transactional
    void testOptimization_noProducts() {
        // PT-BR: Nenhum produto cadastrado. O resultado deve ser vazio, sem erros.
        // EN-US: No products registered. The result should be empty, without errors.
        createMaterial("MP001", "Flour", 500, "g");

        OptimizationResultDTO result = service.optimize();

        assertNotNull(result);
        assertEquals(0, BigDecimal.ZERO.compareTo(result.totalRevenue));
        assertTrue(result.productionPlan.isEmpty());
    }

    @Test
    @Transactional
    void testOptimization_multipleConstraints() {
        // PT-BR: Um produto limitado por multiplas materias-primas. O gargalo
        //        (recurso mais escasso) deve determinar a producao maxima.
        //        Farinha permite 10 unidades, acucar permite 5 -- gargalo e o acucar.
        // EN-US: One product limited by multiple raw materials. The bottleneck
        //        (scarcest resource) must determine the maximum production.
        //        Flour allows 10 units, sugar allows 5 -- bottleneck is sugar.
        RawMaterial flour = createMaterial("MP001", "Flour", 1000, "g");
        RawMaterial sugar = createMaterial("MP002", "Sugar", 500, "g");

        Product cake = createProduct("PROD001", "Cake", 50.00);
        addComposition(cake, flour, 100);
        addComposition(cake, sugar, 100);

        OptimizationResultDTO result = service.optimize();

        assertNotNull(result);
        assertEquals(0, new BigDecimal("250.00").compareTo(result.totalRevenue));
        assertEquals(5, result.productionPlan.get(0).quantityToProduce);
    }

    @Test
    @Transactional
    void testOptimization_materialUsageIsCorrect() {
        // PT-BR: Verificamos se o relatorio de uso de materiais esta correto.
        //        O gerente precisa confiar nesses numeros para tomar decisoes.
        // EN-US: We verify that the material usage report is correct.
        //        The manager needs to trust these numbers to make decisions.
        RawMaterial flour = createMaterial("MP001", "Flour", 500, "g");
        Product bread = createProduct("PROD001", "Bread", 10.00);
        addComposition(bread, flour, 100);

        OptimizationResultDTO result = service.optimize();

        assertNotNull(result);
        assertFalse(result.materialUsage.isEmpty());

        OptimizationResultDTO.MaterialUsage flourUsage = result.materialUsage.stream()
            .filter(m -> m.rawMaterialName.equals("Flour"))
            .findFirst()
            .orElseThrow();

        assertEquals(0, new BigDecimal("500.0000").compareTo(flourUsage.totalUsed));
        assertEquals(0, new BigDecimal("0.0000").compareTo(flourUsage.remaining));
        assertEquals(100.0, flourUsage.usagePercentage, 0.1);
    }

    @Test
    @Transactional
    void testOptimization_computationTimeIsTracked() {
        // PT-BR: O tempo de computacao deve ser rastreado e retornado no resultado.
        // EN-US: Computation time should be tracked and returned in the result.
        RawMaterial flour = createMaterial("MP001", "Flour", 500, "g");
        Product bread = createProduct("PROD001", "Bread", 10.00);
        addComposition(bread, flour, 100);

        OptimizationResultDTO result = service.optimize();

        assertNotNull(result);
        assertTrue(result.computationTimeMs >= 0);
    }

    @Test
    @Transactional
    void testOptimization_productWithoutComposition_isIgnored() {
        // PT-BR: Produtos sem composicao devem ser ignorados pela otimizacao.
        //        Nao faz sentido incluir um produto que nao consome materias-primas.
        // EN-US: Products without composition should be ignored by the optimization.
        //        It makes no sense to include a product that doesn't consume raw materials.
        createMaterial("MP001", "Flour", 500, "g");
        createProduct("PROD001", "Ghost Product", 1000.00);

        OptimizationResultDTO result = service.optimize();

        assertNotNull(result);
        assertEquals(0, BigDecimal.ZERO.compareTo(result.totalRevenue));
    }

    // PT-BR: Metodos auxiliares para criar dados de teste de forma limpa e legivel
    // EN-US: Helper methods to create test data in a clean and readable way
    private RawMaterial createMaterial(String code, String name, double quantity, String unit) {
        RawMaterial material = new RawMaterial();
        material.code = code;
        material.name = name;
        material.stockQuantity = BigDecimal.valueOf(quantity);
        material.unit = unit;
        rawMaterialRepository.persist(material);
        return material;
    }

    private Product createProduct(String code, String name, double price) {
        Product product = new Product();
        product.code = code;
        product.name = name;
        product.salePrice = BigDecimal.valueOf(price);
        product.compositions = new ArrayList<>();
        productRepository.persist(product);
        return product;
    }

    private void addComposition(Product product, RawMaterial material, double quantity) {
        ProductComposition composition = new ProductComposition();
        composition.product = product;
        composition.rawMaterial = material;
        composition.requiredQuantity = BigDecimal.valueOf(quantity);
        product.compositions.add(composition);
        productRepository.persist(product);
    }
}
