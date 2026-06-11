package br.com.maganin.erp.estoque.domain;

import br.com.maganin.erp.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "configuracoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracaoEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String chave;

    @Column(nullable = false, length = 255)
    private String valor;
}
