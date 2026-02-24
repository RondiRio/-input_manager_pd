package com.projedata.inputmanager.service;

import com.projedata.inputmanager.dto.RawMaterialDTO;
import com.projedata.inputmanager.exception.ResourceNotFoundException;
import com.projedata.inputmanager.model.RawMaterial;
import com.projedata.inputmanager.repository.ProductCompositionRepository;
import com.projedata.inputmanager.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// PT-BR: Camada de servico para materias-primas. Aqui ficam as regras de negocio e a
//        conversao entre entidades e DTOs. Preferimos manter os controllers finos e os
//        servicos gordos -- assim fica mais facil testar a logica sem depender do HTTP.
// EN-US: Service layer for raw materials. This is where business rules and entity-to-DTO
//        conversion live. We prefer keeping controllers thin and services fat -- this way
//        it's easier to test the logic without depending on HTTP.
@ApplicationScoped
public class RawMaterialService {

    @Inject
    RawMaterialRepository repository;

    @Inject
    ProductCompositionRepository compositionRepository;

    public List<RawMaterialDTO> listAll() {
        return repository.listAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public RawMaterialDTO findById(Long id) {
        RawMaterial entity = repository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Raw material", id);
        }
        return toDTO(entity);
    }

    @Transactional
    public RawMaterialDTO create(RawMaterialDTO dto) {
        // PT-BR: Verificamos se ja existe uma materia-prima com o mesmo codigo.
        //        Codigos duplicados causariam confusao no chao de fabrica.
        // EN-US: We check if a raw material with the same code already exists.
        //        Duplicate codes would cause confusion on the factory floor.
        RawMaterial existing = repository.findByCode(dto.code);
        if (existing != null) {
            throw new IllegalArgumentException("A raw material with code '" + dto.code + "' already exists");
        }

        RawMaterial entity = toEntity(dto);
        repository.persist(entity);
        return toDTO(entity);
    }

    @Transactional
    public RawMaterialDTO update(Long id, RawMaterialDTO dto) {
        RawMaterial entity = repository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Raw material", id);
        }

        // PT-BR: Se o codigo mudou, garantimos que o novo codigo nao conflita com outro
        // EN-US: If the code changed, we ensure the new code doesn't conflict with another
        if (!entity.code.equals(dto.code)) {
            RawMaterial existing = repository.findByCode(dto.code);
            if (existing != null) {
                throw new IllegalArgumentException("A raw material with code '" + dto.code + "' already exists");
            }
        }

        entity.code = dto.code;
        entity.name = dto.name;
        entity.stockQuantity = dto.stockQuantity;
        entity.unit = dto.unit;
        repository.persist(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        RawMaterial entity = repository.findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Raw material", id);
        }

        // PT-BR: Antes de excluir, verificamos se a materia-prima esta sendo usada em
        //        algum produto. Deletar um insumo que faz parte de uma receita quebraria
        //        a integridade dos dados e causaria problemas na producao.
        // EN-US: Before deleting, we check if the raw material is being used in any
        //        product. Deleting an input that's part of a recipe would break data
        //        integrity and cause production issues.
        long usageCount = compositionRepository.countByRawMaterialId(id);
        if (usageCount > 0) {
            throw new IllegalArgumentException(
                "Cannot delete raw material '" + entity.name +
                "' because it is used in " + usageCount + " product composition(s)"
            );
        }

        repository.delete(entity);
    }

    private RawMaterialDTO toDTO(RawMaterial entity) {
        RawMaterialDTO dto = new RawMaterialDTO();
        dto.id = entity.id;
        dto.code = entity.code;
        dto.name = entity.name;
        dto.stockQuantity = entity.stockQuantity;
        dto.unit = entity.unit;
        return dto;
    }

    private RawMaterial toEntity(RawMaterialDTO dto) {
        RawMaterial entity = new RawMaterial();
        entity.code = dto.code;
        entity.name = dto.name;
        entity.stockQuantity = dto.stockQuantity;
        entity.unit = dto.unit;
        return entity;
    }
}
