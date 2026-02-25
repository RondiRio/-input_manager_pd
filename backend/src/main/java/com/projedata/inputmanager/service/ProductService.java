package com.projedata.inputmanager.service;

import com.projedata.inputmanager.dto.ProductCompositionDTO;
import com.projedata.inputmanager.dto.ProductDTO;
import com.projedata.inputmanager.exception.ResourceNotFoundException;
import com.projedata.inputmanager.model.Product;
import com.projedata.inputmanager.model.ProductComposition;
import com.projedata.inputmanager.model.RawMaterial;
import com.projedata.inputmanager.repository.ProductRepository;
import com.projedata.inputmanager.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// PT-BR: Camada de servico para produtos. A parte mais delicada aqui e gerenciar a
//        composicao do produto (a "receita"). Quando o usuario edita um produto, ele pode
//        adicionar, remover ou alterar ingredientes, e precisamos sincronizar tudo isso
//        com o banco de dados de forma limpa.
// EN-US: Service layer for products. The most delicate part here is managing the product
//        composition (the "recipe"). When the user edits a product, they can add, remove,
//        or change ingredients, and we need to sync all of this with the database cleanly.
@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository repository;

    @Inject
    RawMaterialRepository rawMaterialRepository;

    @Inject
    EntityManager entityManager;

    public List<ProductDTO> listAll() {
        return repository.listAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public ProductDTO findById(Long id) {
        Product entity = repository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Product", id);
        }
        return toDTO(entity);
    }

    @Transactional
    public ProductDTO create(ProductDTO dto) {
        Product existing = repository.findByCode(dto.code);
        if (existing != null) {
            throw new IllegalArgumentException("A product with code '" + dto.code + "' already exists");
        }

        Product entity = new Product();
        entity.code = dto.code;
        entity.name = dto.name;
        entity.salePrice = dto.salePrice;
        entity.compositions = new ArrayList<>();

        // PT-BR: Para cada item da composicao, buscamos a materia-prima no banco.
        //        Se alguma nao existir, avisamos o usuario imediatamente em vez de
        //        deixar o erro aparecer so depois, no commit da transacao.
        // EN-US: For each composition item, we look up the raw material in the database.
        //        If any doesn't exist, we tell the user immediately instead of letting
        //        the error appear only later, at transaction commit time.
        if (dto.compositions != null) {
            for (ProductCompositionDTO compDTO : dto.compositions) {
                ProductComposition comp = buildComposition(entity, compDTO);
                entity.compositions.add(comp);
            }
        }

        repository.persist(entity);
        return toDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Product entity = repository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Product", id);
        }

        if (!entity.code.equals(dto.code)) {
            Product existing = repository.findByCode(dto.code);
            if (existing != null) {
                throw new IllegalArgumentException("A product with code '" + dto.code + "' already exists");
            }
        }

        entity.code = dto.code;
        entity.name = dto.name;
        entity.salePrice = dto.salePrice;

        // PT-BR: A estrategia aqui e "limpar e reconstruir": removemos todas as composicoes
        //        antigas e adicionamos as novas. O flush() entre o clear e o add e essencial --
        //        sem ele, o Hibernate pode tentar inserir as novas composicoes ANTES de deletar
        //        as antigas, violando a UniqueConstraint(product_id, raw_material_id).
        // EN-US: The strategy here is "clear and rebuild": we remove all old compositions
        //        and add the new ones. The flush() between clear and add is essential --
        //        without it, Hibernate may try to insert new compositions BEFORE deleting
        //        the old ones, violating the UniqueConstraint(product_id, raw_material_id).
        entity.compositions.clear();
        entityManager.flush();

        if (dto.compositions != null) {
            for (ProductCompositionDTO compDTO : dto.compositions) {
                ProductComposition comp = buildComposition(entity, compDTO);
                entity.compositions.add(comp);
            }
        }

        repository.persist(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        Product entity = repository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Product", id);
        }
        repository.delete(entity);
    }

    // PT-BR: Monta uma composicao a partir do DTO, validando que a materia-prima existe
    // EN-US: Builds a composition from the DTO, validating that the raw material exists
    private ProductComposition buildComposition(Product product, ProductCompositionDTO dto) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(dto.rawMaterialId);
        if (rawMaterial == null) {
            throw new ResourceNotFoundException("Raw material", dto.rawMaterialId);
        }

        ProductComposition comp = new ProductComposition();
        comp.product = product;
        comp.rawMaterial = rawMaterial;
        comp.requiredQuantity = dto.requiredQuantity;
        return comp;
    }

    private ProductDTO toDTO(Product entity) {
        ProductDTO dto = new ProductDTO();
        dto.id = entity.id;
        dto.code = entity.code;
        dto.name = entity.name;
        dto.salePrice = entity.salePrice;

        if (entity.compositions != null) {
            dto.compositions = entity.compositions.stream()
                .map(this::toCompositionDTO)
                .collect(Collectors.toList());
        } else {
            dto.compositions = new ArrayList<>();
        }

        return dto;
    }

    private ProductCompositionDTO toCompositionDTO(ProductComposition comp) {
        ProductCompositionDTO dto = new ProductCompositionDTO();
        dto.id = comp.id;
        dto.rawMaterialId = comp.rawMaterial.id;
        dto.rawMaterialName = comp.rawMaterial.name;
        dto.rawMaterialUnit = comp.rawMaterial.unit;
        dto.requiredQuantity = comp.requiredQuantity;
        return dto;
    }
}
