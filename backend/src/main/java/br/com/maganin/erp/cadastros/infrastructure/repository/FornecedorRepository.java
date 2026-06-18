package br.com.maganin.erp.cadastros.infrastructure.repository;

import br.com.maganin.erp.cadastros.domain.FornecedorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FornecedorRepository extends JpaRepository<FornecedorEntity, UUID> {

    boolean existsByNomeIgnoreCase(String nome);
    boolean existsByCnpj(String cnpj);
    Page<FornecedorEntity> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
