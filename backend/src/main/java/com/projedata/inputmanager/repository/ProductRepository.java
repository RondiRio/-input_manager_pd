package com.projedata.inputmanager.repository;

import com.projedata.inputmanager.model.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

// PT-BR: Repositorio para produto. Mesma ideia do RawMaterialRepository -- Panache cuida
//        do trabalho pesado e nos cuidamos do que e especifico do negocio.
// EN-US: Repository for product. Same idea as RawMaterialRepository -- Panache handles
//        the heavy lifting and we handle what's specific to the business.
@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public Product findByCode(String code) {
        return find("code", code).firstResult();
    }
}
