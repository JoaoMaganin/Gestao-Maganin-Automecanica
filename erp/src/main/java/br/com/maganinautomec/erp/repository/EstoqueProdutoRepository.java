package br.com.maganinautomec.erp.repository;

import br.com.maganinautomec.erp.entity.EstoqueProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueProdutoRepository extends JpaRepository<EstoqueProdutoEntity, Long> {
}
