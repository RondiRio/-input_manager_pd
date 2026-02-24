package com.projedata.inputmanager.repository;

import com.projedata.inputmanager.model.ProductComposition;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

// PT-BR: Repositorio para composicao de produto. Usado principalmente para consultas
//        especificas, como verificar se uma materia-prima esta sendo usada antes de
//        permitir sua exclusao.
// EN-US: Repository for product composition. Used mainly for specific queries, like
//        checking if a raw material is being used before allowing its deletion.
@ApplicationScoped
public class ProductCompositionRepository implements PanacheRepository<ProductComposition> {

    public List<ProductComposition> findByProductId(Long productId) {
        return list("product.id", productId);
    }

    public long countByRawMaterialId(Long rawMaterialId) {
        return count("rawMaterial.id", rawMaterialId);
    }
}
