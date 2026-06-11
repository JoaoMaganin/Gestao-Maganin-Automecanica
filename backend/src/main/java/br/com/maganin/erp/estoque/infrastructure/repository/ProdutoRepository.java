package br.com.maganin.erp.estoque.infrastructure.repository;

import br.com.maganin.erp.estoque.domain.ProdutoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, UUID> {
    Boolean existsByNomeIgnoreCase(String nome);
    Page<ProdutoEntity> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
