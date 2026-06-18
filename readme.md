# Gestão Maganin Automecânica

Sistema modular para gestão completa de uma oficina mecânica. Desenvolvido com foco em boas práticas de arquitetura, qualidade de código e escalabilidade.

## Sobre o projeto

O sistema nasceu de uma necessidade real — gerenciar o dia a dia de uma oficina mecânica de forma integrada. A primeira versão foi construída sem planejamento de arquitetura. Esta é a segunda versão, reescrita do zero com decisões técnicas conscientes documentadas ao longo do desenvolvimento.

A estratégia adotada foi **Monolito Modular**: cada módulo de negócio é completamente isolado com suas próprias entidades, serviços e controllers, mas todos rodam no mesmo processo. Quando o domínio estiver estável, a extração para microsserviços será feita com segurança.

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.5 |
| Banco de dados | PostgreSQL 17 |
| Migrations | Flyway |
| Segurança | Spring Security + JWT |
| Mapeamento | MapStruct |
| Documentação | SpringDoc OpenAPI (Swagger) |
| Frontend | Angular 17+ com PrimeNG *(em desenvolvimento)* |

## Arquitetura

### Estrutura de módulos

```
br.com.maganin.erp/
├── shared/                  → Kernel compartilhado entre todos os módulos
│   ├── domain/              → BaseEntity com id (UUID), createdAt, updatedAt
│   ├── dto/                 → ApiResponse<T> e PageResponse<T>
│   ├── exception/           → BusinessException e GlobalExceptionHandler
│   └── config/              → JpaConfig, WebConfig (CORS), SecurityConfig
│
├── auth/                    → Autenticação e autorização (em desenvolvimento)
│
├── cadastros/               → Módulo de cadastros base
│   └── fornecedor/          → CRUD de fornecedores
│
└── estoque/                 → Módulo de estoque
    ├── categoria/           → CRUD de categorias de produto
    ├── produto/             → Gestão de produtos com precificação
    ├── movimentacao/        → Histórico de movimentações
    └── configuracao/        → Configurações do sistema (margem padrão etc)
```

### Padrão de camadas

Cada módulo segue a mesma estrutura interna:

```
modulo/
├── domain/                  → Entidades JPA e enums
├── application/
│   ├── dto/                 → Request e Response por funcionalidade
│   ├── mapper/              → Interfaces MapStruct (Entity ↔ DTO)
│   └── service/             → Interface do service
│       └── impl/            → Implementação com regras de negócio
├── infrastructure/
│   └── repository/          → Interfaces JpaRepository
└── api/                     → Controllers REST
```

### Padrão de resposta

Toda resposta da API segue o mesmo envelope:

```json
{
  "success": true,
  "data": { },
  "message": "Operação realizada com sucesso",
  "timestamp": "2026-06-11T20:00:00"
}
```

Respostas paginadas incluem metadados de paginação dentro do `data`.

## Módulos implementados

### Shared

Kernel compartilhado que fornece a base para todos os módulos:

- `BaseEntity` — classe pai de todas as entidades com `id (UUID)`, `createdAt` e `updatedAt` preenchidos automaticamente via Spring Auditing
- `ApiResponse<T>` — envelope padrão de resposta com métodos estáticos `ok()` e `error()`
- `PageResponse<T>` — envelope para respostas paginadas
- `BusinessException` — exceção customizada que carrega mensagem e `HttpStatus`
- `GlobalExceptionHandler` — captura todas as exceções e retorna `ApiResponse` padronizado
- `JpaConfig` — habilita auditoria automática (`@EnableJpaAuditing`)
- `WebConfig` — CORS configurado para o frontend Angular em `localhost:4200`

### Cadastros — Fornecedor

Gerenciamento de fornecedores com validação de duplicidade por nome.

**Endpoints:**

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/fornecedores` | Cadastrar fornecedor |
| GET | `/api/fornecedores` | Listar com paginação e filtro por nome |
| GET | `/api/fornecedores/{id}` | Buscar por ID |
| PUT | `/api/fornecedores/{id}` | Atualizar dados |
| PATCH | `/api/fornecedores/{id}/ativar` | Ativar fornecedor |
| PATCH | `/api/fornecedores/{id}/desativar` | Desativar fornecedor |

### Estoque — Categoria

Categorização de produtos com ativação/desativação em vez de exclusão física.

**Endpoints:**

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/categorias` | Cadastrar categoria |
| GET | `/api/categorias` | Listar com paginação e filtro por nome |
| GET | `/api/categorias/{id}` | Buscar por ID |
| PUT | `/api/categorias/{id}` | Atualizar dados |
| PATCH | `/api/categorias/{id}/ativar` | Ativar categoria |
| PATCH | `/api/categorias/{id}/desativar` | Desativar categoria |

### Estoque — Produto

Gestão completa de produtos com lógica de precificação por Preço Médio Ponderado.

**Regras de negócio:**

- Preço de venda nunca pode ser menor que o preço de custo
- A cada entrada de estoque o PMP é recalculado automaticamente
- Preço de venda sugerido é calculado aplicando a margem de lucro sobre o PMP
- Margem pode ser definida por produto ou herdar a margem padrão global
- Toda entrada gera uma `MovimentacaoEntity` do tipo `ENTRADA` automaticamente
- Entrada de estoque é uma operação `@Transactional` — estoque e movimentação são salvos juntos ou nenhum é salvo

**Cálculo do PMP:**
```
novo PMP = (quantidade atual × custo médio atual + quantidade entrada × custo entrada)
           / (quantidade atual + quantidade entrada)
```

**Cálculo do preço sugerido:**
```
preço sugerido = PMP × (1 + margem / 100)
```

**Endpoints:**

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/produtos` | Cadastrar produto |
| GET | `/api/produtos` | Listar com paginação e filtro por nome |
| GET | `/api/produtos/{id}` | Buscar por ID |
| PUT | `/api/produtos/{id}` | Atualizar dados e preço |
| PATCH | `/api/produtos/{id}/ativar` | Ativar produto |
| PATCH | `/api/produtos/{id}/desativar` | Desativar produto |
| POST | `/api/produtos/entrada` | Registrar entrada de estoque |

### Estoque — Configurações

Tabela de chave/valor para configurações do sistema. Atualmente armazena:

| Chave | Valor padrão | Descrição |
|---|---|---|
| `margem_padrao` | `30` | Margem de lucro padrão aplicada a todos os produtos que não têm margem específica |

## Migrations

| Versão | Descrição |
|---|---|
| V1 | Schema base — tabelas de usuários, categorias, produtos, movimentações e fornecedores |
| V2 | Tabela de configurações + insert da margem padrão |
| V3 | Colunas `margem_lucro` e `preco_custo_medio` na tabela de produtos |

## Setup local

### Pré-requisitos

- Java 21
- Maven 3.9+
- PostgreSQL 17

### Banco de dados

```sql
CREATE DATABASE gestao_maganin;
```

### Configuração

As configurações de banco estão em `backend/src/main/resources/application.yml`. Por padrão:

```yaml
datasource:
  url: jdbc:postgresql://localhost:5432/gestao_maganin
  username: postgres
  password: postgres
```

Ajusta as credenciais se necessário.

### Subindo o backend

```bash
cd backend
mvn spring-boot:run
```

A aplicação sobe na porta `8080`. O Flyway executa as migrations automaticamente.

**Swagger:** `http://localhost:8080/swagger-ui/index.html`

## Decisões técnicas

**UUID como chave primária** — todos os IDs são UUID gerados pelo banco. Facilita a extração futura para microsserviços onde IDs sequenciais de bancos diferentes colidiriam.

**Flyway para migrations** — nenhuma alteração de banco é feita manualmente ou pelo Hibernate (`ddl-auto: validate`). Todo histórico de schema está versionado no repositório.

**Interface + Impl nos services** — cada service tem uma interface e uma implementação separada. O controller depende da interface, não da implementação. Facilita testes unitários com mocks e permite trocar a implementação sem alterar quem consome o service.

**MapStruct para mapeamento** — conversão entre entidades e DTOs gerada em tempo de compilação. Zero reflexão, sem overhead de runtime.

**GlobalExceptionHandler centralizado** — nenhum `try/catch` nos controllers. Todas as exceções sobem e são tratadas em um único lugar com resposta padronizada.

**Soft delete** — nenhum registro é deletado fisicamente. Categorias, produtos e fornecedores têm o campo `ativo` que é setado para `false` em vez de excluir o registro. Preserva histórico e integridade referencial.

## Próximos passos

- [ ] Autenticação JWT
- [ ] Módulo de ajuste manual de estoque
- [ ] Módulo de Clientes e Veículos
- [ ] Módulo de Ordens de Serviço
- [ ] Módulo Financeiro
- [ ] Frontend Angular com PrimeNG
- [ ] Docker Compose
- [ ] Deploy