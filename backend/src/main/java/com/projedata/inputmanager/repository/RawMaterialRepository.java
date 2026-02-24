package com.projedata.inputmanager.repository;

import com.projedata.inputmanager.model.RawMaterial;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

// PT-BR: Repositorio para materia-prima. Panache ja nos da de graca os metodos basicos
//        como findById, listAll, persist, delete. Aqui so adicionamos o que e especifico
//        do nosso dominio, como busca por codigo.
// EN-US: Repository for raw material. Panache already gives us the basic methods like
//        findById, listAll, persist, delete for free. Here we only add what's specific
//        to our domain, like search by code.
@ApplicationScoped
public class RawMaterialRepository implements PanacheRepository<RawMaterial> {

    public RawMaterial findByCode(String code) {
        return find("code", code).firstResult();
    }
}
