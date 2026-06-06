package br.com.maganin.erp.estoque.domain;

import br.com.maganin.erp.shared.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "configuracoes")
public class ConfiguracaoEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String chave;

    @Column(nullable = false, length = 255)
    private String valor;
}
