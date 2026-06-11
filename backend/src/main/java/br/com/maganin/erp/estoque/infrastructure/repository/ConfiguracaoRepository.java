package br.com.maganin.erp.estoque.infrastructure.repository;

import br.com.maganin.erp.estoque.domain.ConfiguracaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConfiguracaoRepository extends JpaRepository<ConfiguracaoEntity, UUID> {
    Optional<ConfiguracaoEntity> findByChave(String chave);
}
