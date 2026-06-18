package br.com.maganin.erp.cadastros.domain;

import br.com.maganin.erp.estoque.domain.ProdutoEntity;
import br.com.maganin.erp.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fornecedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorEntity extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(unique = true, length = 14)
    private String cnpj;

    @Column(unique = true, length = 11)
    private String telefone;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProdutoEntity> produtos = new ArrayList<>();
}
