package br.com.maganin.erp.estoque.infrastructure.repository;

import br.com.maganin.erp.estoque.domain.CategoriaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, UUID> {

    // O Spring implementa a query automaticamente com base no nome do método
    boolean existsByNomeIgnoreCase(String nome);
    Page<CategoriaEntity> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}