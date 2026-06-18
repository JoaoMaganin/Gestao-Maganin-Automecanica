package br.com.maganin.erp.estoque.domain;

import br.com.maganin.erp.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    public String nome;

    @Column(columnDefinition = "TEXT")
    public String descricao;

    @Column(nullable = false)
    public Boolean ativo;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProdutoEntity> produtos = new ArrayList<>();
}
