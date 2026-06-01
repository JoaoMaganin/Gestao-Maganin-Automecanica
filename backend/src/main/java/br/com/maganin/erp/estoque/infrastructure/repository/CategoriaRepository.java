package br.com.maganin.erp.estoque.infrastructure.repository;

import br.com.maganin.erp.estoque.domain.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, UUID> {

}