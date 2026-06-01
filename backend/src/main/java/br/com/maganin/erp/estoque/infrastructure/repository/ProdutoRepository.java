package br.com.maganin.erp.estoque.infrastructure.repository;

import br.com.maganin.erp.estoque.domain.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, UUID> {

}
