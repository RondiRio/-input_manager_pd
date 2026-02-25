package com.projedata.inputmanager.service;

import com.projedata.inputmanager.dto.OptimizationResultDTO;
import com.projedata.inputmanager.model.Product;
import com.projedata.inputmanager.model.ProductComposition;
import com.projedata.inputmanager.model.RawMaterial;
import com.projedata.inputmanager.repository.ProductRepository;
import com.projedata.inputmanager.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

// PT-BR: Servico de otimizacao de producao. Este e o coracao do sistema -- o algoritmo
//        que decide o que a fabrica deve produzir para maximizar o lucro com os insumos
//        disponiveis. Funciona como o problema da mochila multidimensional, resolvido
//        com Branch and Bound para encontrar a solucao otima exata.
//
//        Imagine que voce tem uma mochila (o estoque) e varios itens (os produtos) com
//        diferentes pesos (consumo de materias-primas) e valores (precos de venda).
//        O algoritmo encontra a melhor combinacao possivel.
//
// EN-US: Production optimization service. This is the heart of the system -- the algorithm
//        that decides what the factory should produce to maximize profit with the available
//        inputs. It works like a multi-dimensional knapsack problem, solved with Branch and
//        Bound to find the exact optimal solution.
//
//        Imagine you have a backpack (the inventory) and several items (the products) with
//        different weights (raw material consumption) and values (sale prices).
//        The algorithm finds the best possible combination.
@ApplicationScoped
public class ProductionOptimizationService {

    private static final int MAX_NODES = 100_000;
    private static final long MAX_COMPUTATION_TIME_MS = 10_000;

    @Inject
    ProductRepository productRepository;

    @Inject
    RawMaterialRepository rawMaterialRepository;

    // PT-BR: Nodo na arvore de busca do Branch and Bound. Cada nodo representa uma decisao
    //        parcial: para os primeiros 'level' produtos, ja decidimos quantas unidades
    //        fabricar. O upperBound nos diz o melhor cenario possivel a partir deste nodo.
    // EN-US: Node in the Branch and Bound search tree. Each node represents a partial
    //        decision: for the first 'level' products, we've already decided how many units
    //        to manufacture. The upperBound tells us the best possible scenario from this node.
    private static class BBNode {
        int[] production;
        int level;
        double currentRevenue;
        double upperBound;

        BBNode(int[] production, int level, double currentRevenue) {
            this.production = production;
            this.level = level;
            this.currentRevenue = currentRevenue;
        }
    }

    public OptimizationResultDTO optimize() {
        long startTime = System.currentTimeMillis();

        List<Product> allProducts = productRepository.listAll();
        List<RawMaterial> allMaterials = rawMaterialRepository.listAll();

        // PT-BR: Filtramos apenas os produtos que tem composicao definida.
        //        Um produto sem receita nao faz sentido na otimizacao.
        // EN-US: We filter only products that have a defined composition.
        //        A product without a recipe doesn't make sense in the optimization.
        List<Product> products = allProducts.stream()
            .filter(p -> p.compositions != null && !p.compositions.isEmpty())
            .toList();

        if (products.isEmpty() || allMaterials.isEmpty()) {
            return buildEmptyResult(allMaterials, startTime);
        }

        // PT-BR: Construimos um mapa de indice para acessar rapidamente os dados das
        //        materias-primas. Isso evita buscas O(n) repetidas durante a otimizacao.
        // EN-US: We build an index map for quick access to raw material data.
        //        This avoids repeated O(n) lookups during optimization.
        Map<Long, Integer> materialIndexMap = new HashMap<>();
        for (int j = 0; j < allMaterials.size(); j++) {
            materialIndexMap.put(allMaterials.get(j).id, j);
        }

        int n = products.size();
        int m = allMaterials.size();

        double[] prices = new double[n];
        double[] stock = new double[m];
        double[][] consumption = new double[m][n];

        for (int j = 0; j < m; j++) {
            stock[j] = allMaterials.get(j).stockQuantity.doubleValue();
        }

        for (int i = 0; i < n; i++) {
            prices[i] = products.get(i).salePrice.doubleValue();

            for (ProductComposition comp : products.get(i).compositions) {
                Integer matIndex = materialIndexMap.get(comp.rawMaterial.id);
                if (matIndex != null) {
                    consumption[matIndex][i] = comp.requiredQuantity.doubleValue();
                }
            }
        }

        // PT-BR: Calculamos o maximo de unidades que cada produto pode ter individualmente,
        //        considerando cada materia-prima como gargalo potencial.
        // EN-US: We calculate the maximum units each product can have individually,
        //        considering each raw material as a potential bottleneck.
        int[] maxUnits = new int[n];
        for (int i = 0; i < n; i++) {
            maxUnits[i] = Integer.MAX_VALUE;
            for (int j = 0; j < m; j++) {
                if (consumption[j][i] > 0) {
                    maxUnits[i] = Math.min(maxUnits[i], (int) (stock[j] / consumption[j][i]));
                }
            }
            if (maxUnits[i] == Integer.MAX_VALUE) {
                maxUnits[i] = 0;
            }
        }

        int[] bestSolution = branchAndBound(n, m, prices, stock, consumption, maxUnits, startTime);
        double bestRevenue = 0;
        for (int i = 0; i < n; i++) {
            bestRevenue += prices[i] * bestSolution[i];
        }

        return buildResult(products, allMaterials, bestSolution, bestRevenue,
                          consumption, stock, materialIndexMap, startTime);
    }

    // PT-BR: O Branch and Bound funciona assim: exploramos uma arvore de decisoes onde
    //        cada nivel decide quantas unidades de um produto fabricar. Em cada nodo,
    //        calculamos um limite superior (usando relaxacao linear) do melhor resultado
    //        possivel. Se esse limite e pior que a melhor solucao ja encontrada, podamos
    //        esse ramo inteiro -- nao vale a pena explorar mais.
    //
    //        Usamos busca pelo melhor primeiro (best-first search) para encontrar boas
    //        solucoes rapidamente, o que melhora a poda e acelera a convergencia.
    //
    // EN-US: Branch and Bound works like this: we explore a decision tree where each
    //        level decides how many units of a product to manufacture. At each node,
    //        we calculate an upper bound (using linear relaxation) of the best possible
    //        result. If this bound is worse than the best solution already found, we prune
    //        that entire branch -- it's not worth exploring further.
    //
    //        We use best-first search to find good solutions quickly, which improves
    //        pruning and accelerates convergence.
    private int[] branchAndBound(int n, int m, double[] prices, double[] stock,
                                  double[][] consumption, int[] maxUnits, long startTime) {

        PriorityQueue<BBNode> queue = new PriorityQueue<>(
            Comparator.comparingDouble(node -> -node.upperBound)
        );

        int[] rootProduction = new int[n];
        BBNode root = new BBNode(rootProduction, 0, 0.0);
        root.upperBound = calculateUpperBound(root, n, m, prices, stock, consumption);
        queue.add(root);

        double bestRevenue = 0;
        int[] bestSolution = new int[n];
        int nodesExplored = 0;

        while (!queue.isEmpty()) {
            // PT-BR: Verificamos os limites de tempo e nos para evitar travar o servidor
            // EN-US: Check time and node limits to avoid locking up the server
            if (nodesExplored >= MAX_NODES) break;
            if (System.currentTimeMillis() - startTime > MAX_COMPUTATION_TIME_MS) break;

            BBNode node = queue.poll();
            nodesExplored++;

            if (node.upperBound <= bestRevenue) continue;

            if (node.level == n) {
                if (node.currentRevenue > bestRevenue) {
                    bestRevenue = node.currentRevenue;
                    bestSolution = Arrays.copyOf(node.production, n);
                }
                continue;
            }

            int productIndex = node.level;

            for (int qty = 0; qty <= maxUnits[productIndex]; qty++) {
                boolean feasible = checkFeasibility(node, productIndex, qty, m, consumption, stock);

                if (!feasible) break;

                int[] childProd = Arrays.copyOf(node.production, n);
                childProd[productIndex] = qty;
                double childRevenue = node.currentRevenue + prices[productIndex] * qty;

                BBNode child = new BBNode(childProd, node.level + 1, childRevenue);
                child.upperBound = calculateUpperBound(child, n, m, prices, stock, consumption);

                if (child.upperBound > bestRevenue) {
                    queue.add(child);
                }
            }
        }

        return bestSolution;
    }

    // PT-BR: Verifica se produzir 'qty' unidades do produto 'productIndex' e viavel,
    //        considerando o que ja foi decidido nos niveis anteriores da arvore.
    // EN-US: Checks if producing 'qty' units of product 'productIndex' is feasible,
    //        considering what was already decided in previous tree levels.
    private boolean checkFeasibility(BBNode node, int productIndex, int qty,
                                      int m, double[][] consumption, double[] stock) {
        for (int j = 0; j < m; j++) {
            double used = 0;
            for (int i = 0; i < node.level; i++) {
                used += consumption[j][i] * node.production[i];
            }
            used += consumption[j][productIndex] * qty;
            if (used > stock[j] + 1e-9) {
                return false;
            }
        }
        return true;
    }

    // PT-BR: Calcula o limite superior (upper bound) valido para o problema da mochila
    //        multidimensional. Para cada produto ainda nao decidido, calculamos
    //        independentemente o maximo fracionario que ele poderia produzir com os
    //        recursos restantes, SEM consumir esses recursos (permitindo "duplicacao").
    //        Isso garante uma SUPERESTIMATIVA -- o bound nunca e menor que a solucao
    //        otima real, portanto nunca podamos um ramo que contenha a solucao otima.
    //
    //        A versao anterior usava preenchimento sequencial (consomia recursos para cada
    //        produto antes de calcular o proximo), o que gerava um bound SUBESTIMADO.
    //        Isso fazia o algoritmo podar ramos com a solucao otima, retornando resultado
    //        subotimo (ex: so produzia o primeiro produto, ignorando combinacoes melhores).
    //
    // EN-US: Calculates a valid upper bound for the multi-dimensional knapsack problem.
    //        For each undecided product, we independently calculate the maximum fractional
    //        units it could produce with the remaining resources, WITHOUT consuming those
    //        resources (allowing "overlap"). This guarantees an OVERESTIMATE -- the bound
    //        is never less than the real optimal solution, so we never prune a branch
    //        that contains the optimal solution.
    //
    //        The previous version used sequential fill (consuming resources for each product
    //        before calculating the next), which generated an UNDERESTIMATE. This caused the
    //        algorithm to prune branches containing the optimal solution, returning suboptimal
    //        results (e.g., only producing the first product, ignoring better combinations).
    private double calculateUpperBound(BBNode node, int n, int m,
                                        double[] prices, double[] stock,
                                        double[][] consumption) {
        double bound = node.currentRevenue;
        double[] remaining = Arrays.copyOf(stock, m);

        for (int i = 0; i < node.level; i++) {
            for (int j = 0; j < m; j++) {
                remaining[j] -= consumption[j][i] * node.production[i];
            }
        }

        // PT-BR: Para cada produto restante, calculamos o maximo INDEPENDENTE que ele
        //        poderia produzir com os recursos disponiveis. Nao atualizamos remaining
        //        porque cada produto calcula isoladamente -- isso da um bound valido
        //        (superestimado) que nunca poda a solucao otima.
        // EN-US: For each remaining product, we calculate the INDEPENDENT maximum it could
        //        produce with available resources. We don't update remaining because each
        //        product calculates in isolation -- this gives a valid (overestimated) bound
        //        that never prunes the optimal solution.
        for (int i = node.level; i < n; i++) {
            double maxFractionalUnits = Double.MAX_VALUE;
            boolean hasConstraint = false;

            for (int j = 0; j < m; j++) {
                if (consumption[j][i] > 1e-9) {
                    hasConstraint = true;
                    maxFractionalUnits = Math.min(maxFractionalUnits, remaining[j] / consumption[j][i]);
                }
            }

            if (!hasConstraint) continue;

            if (maxFractionalUnits > 0) {
                bound += prices[i] * maxFractionalUnits;
                // PT-BR: NAO atualizamos remaining[] aqui! Cada produto usa o estoque
                //        completo independentemente. Isso garante um upper bound valido.
                // EN-US: We do NOT update remaining[] here! Each product uses the full
                //        stock independently. This ensures a valid upper bound.
            }
        }

        return bound;
    }

    private OptimizationResultDTO buildResult(List<Product> products, List<RawMaterial> materials,
                                               int[] solution, double revenue,
                                               double[][] consumption, double[] stock,
                                               Map<Long, Integer> materialIndexMap,
                                               long startTime) {
        OptimizationResultDTO result = new OptimizationResultDTO();
        result.totalRevenue = BigDecimal.valueOf(revenue).setScale(2, RoundingMode.HALF_UP);
        result.computationTimeMs = System.currentTimeMillis() - startTime;

        // PT-BR: Montamos o plano de producao com detalhes de cada produto
        // EN-US: We build the production plan with details for each product
        result.productionPlan = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            OptimizationResultDTO.ProductionItem item = new OptimizationResultDTO.ProductionItem();
            item.productId = p.id;
            item.productName = p.name;
            item.productCode = p.code;
            item.quantityToProduce = solution[i];
            item.unitPrice = p.salePrice;
            item.subtotal = p.salePrice.multiply(BigDecimal.valueOf(solution[i]));
            result.productionPlan.add(item);
        }

        // PT-BR: Calculamos o uso de cada materia-prima para dar visibilidade ao gerente
        // EN-US: We calculate the usage of each raw material to give visibility to the manager
        result.materialUsage = new ArrayList<>();
        for (int j = 0; j < materials.size(); j++) {
            RawMaterial mat = materials.get(j);
            double totalUsed = 0;

            for (int i = 0; i < products.size(); i++) {
                totalUsed += consumption[j][i] * solution[i];
            }

            OptimizationResultDTO.MaterialUsage usage = new OptimizationResultDTO.MaterialUsage();
            usage.rawMaterialId = mat.id;
            usage.rawMaterialName = mat.name;
            usage.totalAvailable = mat.stockQuantity;
            usage.totalUsed = BigDecimal.valueOf(totalUsed).setScale(4, RoundingMode.HALF_UP);
            usage.remaining = mat.stockQuantity.subtract(usage.totalUsed);
            usage.unit = mat.unit;
            usage.usagePercentage = stock[j] > 0 ? (totalUsed / stock[j]) * 100.0 : 0.0;
            result.materialUsage.add(usage);
        }

        return result;
    }

    private OptimizationResultDTO buildEmptyResult(List<RawMaterial> materials, long startTime) {
        OptimizationResultDTO result = new OptimizationResultDTO();
        result.totalRevenue = BigDecimal.ZERO;
        result.computationTimeMs = System.currentTimeMillis() - startTime;
        result.productionPlan = new ArrayList<>();

        result.materialUsage = new ArrayList<>();
        for (RawMaterial mat : materials) {
            OptimizationResultDTO.MaterialUsage usage = new OptimizationResultDTO.MaterialUsage();
            usage.rawMaterialId = mat.id;
            usage.rawMaterialName = mat.name;
            usage.totalAvailable = mat.stockQuantity;
            usage.totalUsed = BigDecimal.ZERO;
            usage.remaining = mat.stockQuantity;
            usage.unit = mat.unit;
            usage.usagePercentage = 0.0;
            result.materialUsage.add(usage);
        }

        return result;
    }
}
