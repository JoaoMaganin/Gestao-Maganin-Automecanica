/* eslint-disable @next/next/no-img-element */
'use client';
import { Button } from 'primereact/button';
import { Column } from 'primereact/column';
import { DataTable } from 'primereact/datatable';
import { Dialog } from 'primereact/dialog';
import { InputNumber, InputNumberValueChangeEvent } from 'primereact/inputnumber';
import { InputText } from 'primereact/inputtext';
import { Toast } from 'primereact/toast';
import { Toolbar } from 'primereact/toolbar';
import { classNames } from 'primereact/utils';
import React, { useEffect, useMemo, useRef, useState } from 'react';
import { EstoqueProdutoService } from '@/service/EstoqueProdutoService';
import { InputMask } from 'primereact/inputmask';


const EstoquePage = () => {
    let produtoVazio: ERP.EstoqueProduto = {
        id: null,
        nomeProduto: '',
        precoCustoProduto: 0.0,
        fornecedor: '',
        quantidadeEstoque: 0,
        dataCompra: new Date().toLocaleDateString('pt-BR', { year: 'numeric', month: '2-digit', day: '2-digit' }).split('/').reverse().join('-'),
        quantidadeVendida: 0,
        precoVendaProduto: 0.0
    };

    const [produtos, setProdutos] = useState<ERP.EstoqueProduto[]>([]);
    const [produtoDialog, setProdutoDialog] = useState(false);
    const [deleteProdutoDialog, setDeleteProdutoDialog] = useState(false);
    const [deleteProdutosDialog, setDeleteProdutosDialog] = useState(false);
    const [produto, setProduto] = useState<ERP.EstoqueProduto>(produtoVazio);
    const [produtosSelecionados, setProdutosSelecionados] = useState<ERP.EstoqueProduto[]>([]);
    const [submitted, setSubmitted] = useState(false);
    const [globalFilter, setGlobalFilter] = useState('');
    const [lucro, setLucro] = useState(0);

    // fix any
    const [filters, setFilters]: any = useState({
        global: { value: "", matchMode: 'contains' }
    });

    const toast = useRef<Toast>(null);
    const dt = useRef<DataTable<any>>(null);
    const estoqueProdutoService = useMemo(() => new EstoqueProdutoService(), []);

    // Atualiza a lista de produtos
    useEffect(() => {
        estoqueProdutoService.listarTodos()
            .then((response) => {
                const produtosComLucro = response.data.map((p: ERP.EstoqueProduto) => ({
                    ...p,
                    lucroPorProduto: (p.precoVendaProduto - p.precoCustoProduto) * p.quantidadeVendida
                }));
                setProdutos(produtosComLucro);
            })
            .catch((erro) => console.log(erro));
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [produto]);

    // Calcula o lucro dinamicamente
    useEffect(() => {
        if (produtos && produtos.length > 0) {
            let totalLucro = produtos.reduce(
                (acc, produto) => acc + ((produto.precoVendaProduto - produto.precoCustoProduto) * produto.quantidadeVendida || 0),
                0
            );
            setLucro(totalLucro);
        }
    }, [produtos]);

    const openNew = () => {
        setProduto(produtoVazio);
        setSubmitted(false);
        setProdutoDialog(true);
    };

    const hideDialog = () => {
        setSubmitted(false);
        setProdutoDialog(false);
    };

    const hideDeleteProdutoDialog = () => {
        setDeleteProdutoDialog(false);
    };

    const hideDeleteProdutosDialog = () => {
        setDeleteProdutosDialog(false);
    };

    const checaProdutoInvalido = (produto: ERP.EstoqueProduto): boolean => {
        if (produto.nomeProduto == '') {
            return true;
        }

        if (produto.fornecedor == '') {
            return true;
        }

        if (produto.quantidadeEstoque < 0) {
            return true;
        }

        if (produto.precoCustoProduto < 0) {
            return true;
        }

        if (produto.quantidadeVendida < 0) {
            return true;
        }

        if (produto.quantidadeVendida > produto.quantidadeEstoque) {
            return true;
        }

        if (produto.precoVendaProduto < 0) {
            return true;
        }

        if (produto.precoVendaProduto <= produto.precoCustoProduto) {
            return true;
        }

        if (!produto.dataCompra) {
            return true;
        }
        return false;
    }

    const checaFormatoDataInvalida = (date: string): boolean => {
        const regex = /^\d{4}-\d{2}-\d{2}$/;
        if (regex.test(date)) return true;

        const d = new Date(date);
        return d instanceof Date && !isNaN(d.getTime()) && date === d.toISOString().split("T")[0];
    };

    const saveProduto = () => {
        setSubmitted(true);

        if (checaProdutoInvalido(produto) && checaFormatoDataInvalida(produto.dataCompra)) {
            return; // impede o salvamento
        }

        if (!produto.id) {
            estoqueProdutoService.criar(produto)
                .then((response) => {
                    setProdutoDialog(false);
                    setProduto(produtoVazio);
                    setProdutos([]);
                    toast.current?.show({
                        severity: 'info',
                        summary: 'Sucesso!',
                        detail: 'Produto registrado com sucesso!'
                    });
                })
                .catch((error) => {
                    console.log(error);
                    toast.current?.show({
                        severity: 'error',
                        summary: 'Erro!',
                        detail: 'Algum valor do produto está inválido.'
                    });
                })
        } else {
            estoqueProdutoService.atualizar(produto)
                .then((response) => {
                    setProdutoDialog(false);
                    setProduto(produtoVazio);
                    setProdutos([]);
                    toast.current?.show({
                        severity: 'info',
                        summary: 'Sucesso!',
                        detail: 'Produto atualizado com sucesso'
                    });
                })
                .catch((error) => {
                    console.log(error.message);
                    toast.current?.show({
                        severity: 'error',
                        summary: 'Erro!',
                        detail: 'Algum valor do produto está inválido.'
                    });
                })
        }
    };

    const editProduto = (produto: ERP.EstoqueProduto) => {
        setProduto({
            ...produto,
            precoVendaProduto: parseFloat(produto.precoVendaProduto.toFixed(2)),
            precoCustoProduto: parseFloat(produto.precoCustoProduto.toFixed(2))
        });
        setProdutoDialog(true);
    };

    const confirmDeleteProduto = (produto: ERP.EstoqueProduto) => {
        setProduto(produto);
        setDeleteProdutoDialog(true);
    };

    const deleteProduto = () => {
        if (produto.id) {
            estoqueProdutoService.deletar(produto.id)
                .then((response) => {
                    setProduto(produtoVazio);
                    setDeleteProdutoDialog(false);
                    setProdutos([]);
                    toast.current?.show({
                        severity: 'success',
                        summary: 'Sucesso!',
                        detail: `Produto ${produto.nomeProduto} deletado`,
                        life: 3000
                    });
                }).catch((error) => {
                    console.log("Erro ao deletar: " + error.message)
                    toast.current?.show({
                        severity: 'error',
                        summary: 'Erro!',
                        detail: `Erro ao deletar o produto ${produto.nomeProduto}`,
                        life: 3000
                    });
                })
        }
    };

    const exportCSV = () => {
        dt.current?.exportCSV();
    };

    const confirmDeleteSelected = () => {
        setDeleteProdutosDialog(true);
    };

    const deleteSelectedProdutos = () => {
        Promise.all(
            produtosSelecionados.map(async (_produto: ERP.EstoqueProduto) => {
                if (_produto.id) {
                    await estoqueProdutoService.deletar(_produto.id);
                }
            })
        ).then((response) => {
            setProdutos([]);
            setProdutosSelecionados([]);
            setDeleteProdutosDialog(false);
            toast.current?.show({
                severity: 'success',
                summary: 'Successful!',
                detail: 'Produtos deletados',
                life: 3000
            })
        }).catch((error) => {
            toast.current?.show({
                severity: 'error',
                summary: 'Error!',
                detail: 'Erro ao deletar produtos',
                life: 3000
            });
        })
    };

    const onInputChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
        nomeProduto: keyof typeof produto // <- chave válida do objeto
    ) => {
        const val = e.target.value || '';
        let _produto = { ...produto };
        _produto[nomeProduto] = val as never; // pode precisar de cast
        setProduto(_produto);
    };

    const onInputNumberChange = (e: InputNumberValueChangeEvent, nomeProduto: keyof typeof produto) => {
        const val = e.value || 0;
        let _produto = { ...produto };
        _produto[nomeProduto] = val as never;
        setProduto(_produto);
    };

    const leftToolbarTemplate = () => {
        return (
            <React.Fragment>
                <div className="my-2">
                    <Button label="Adicionar" icon="pi pi-plus" severity="success" className=" mr-2" onClick={openNew} />
                    <Button label="Deletar" icon="pi pi-trash" severity="danger" className=" mr-2" onClick={confirmDeleteSelected} disabled={!produtosSelecionados || !(produtosSelecionados as any).length} />
                </div>
            </React.Fragment>
        );
    };

    const rightToolbarTemplate = () => {
        return (
            <React.Fragment>
                <Button label="Gerar planilha" icon="pi pi-upload" severity="help" onClick={exportCSV} />
            </React.Fragment>
        );
    };

    // const idBodyTemplate = (rowData: ERP.EstoqueProduto) => {
    //     return (
    //         <>
    //             <span className="p-column-title">Id</span>
    //             {rowData.id}
    //         </>
    //     );
    // };

    const nomeBodyTemplate = (rowData: ERP.EstoqueProduto) => {
        return (
            <>
                <span className="p-column-title">Nome</span>
                {rowData.nomeProduto}
            </>
        );
    };

    const precoCustoBodyTemplate = (rowData: ERP.EstoqueProduto) => {
        return (
            <>
                <span className="p-column-title">Preço</span>
                R$ {rowData.precoCustoProduto}
            </>
        );
    };

    const fornecedorBodyTemplate = (rowData: ERP.EstoqueProduto) => {
        return (
            <>
                <span className="p-column-title">Fornecedor</span>
                {rowData.fornecedor}
            </>
        );
    };

    const quantidadeEstoqueBodyTemplate = (rowData: ERP.EstoqueProduto) => {
        return (
            <>
                <span className="p-column-title">Quantidade em estoque</span>
                {rowData.quantidadeEstoque}
            </>
        );
    };

    const quantidadeVendidaBodyTemplate = (rowData: ERP.EstoqueProduto) => {
        return (
            <>
                <span className="p-column-title">Quantidade em estoque</span>
                {rowData.quantidadeVendida}
            </>
        );
    };

    const precoVendaProdutoBodyTemplate = (rowData: ERP.EstoqueProduto) => {
        return (
            <>
                <span className="p-column-title">Preço</span>
                R$ {rowData.precoVendaProduto}
            </>
        );
    };

    const dataCompraBodyTemplate = (rowData: ERP.EstoqueProduto) => {
        return (
            <>
                <span className="p-column-title">Data de compra</span>
                {rowData.dataCompra}
            </>
        );
    };

    const lucroPorProdutoBodyTemplate = (rowData: ERP.EstoqueProduto) => {
        return (
            <>
                <span className="p-column-title">Preço</span>
                R$ {((rowData.precoVendaProduto - rowData.precoCustoProduto) * rowData.quantidadeVendida).toFixed(2)}
            </>
        );
    };

    const actionBodyTemplate = (rowData: ERP.EstoqueProduto) => {
        return (
            <>
                <Button icon="pi pi-pencil" rounded severity="success" className="mr-2" onClick={() => editProduto(rowData)} />
                <Button icon="pi pi-trash" rounded severity="warning" onClick={() => confirmDeleteProduto(rowData)} />
            </>
        );
    };

    const header = (
        <div className="flex flex-column md:flex-row md:justify-content-between md:align-items-center">
            <h5 className="m-0">Gerenciamento do estoque</h5>
            <h5 className="m-0">Lucro total: R$ {lucro}</h5>
            <span className="block mt-2 md:mt-0 p-input-icon-left">
                <i className="pi pi-search" />
                <InputText
                    type="search"
                    value={(filters.global as any)?.value ?? ""}
                    onInput={(e) => setGlobalFilter(e.currentTarget.value)}
                    onChange={(e) =>
                        setFilters((prev: { global: any; }) => ({
                            ...prev,
                            global: { ...(prev.global as any), value: e.target.value },
                        }))
                    }
                    placeholder="Search..." />
            </span>
        </div>
    );

    const produtoDialogFooter = (
        <>
            <Button label="Cancelar" icon="pi pi-times" text onClick={hideDialog} />
            <Button label="Salvar" icon="pi pi-check" text onClick={saveProduto} />
        </>
    );

    const deleteProdutoDialogFooter = (
        <>
            <Button label="Não" icon="pi pi-times" text onClick={hideDeleteProdutoDialog} />
            <Button label="Sim" icon="pi pi-check" text onClick={deleteProduto} />
        </>
    );

    const deleteProdutosDialogFooter = (
        <>
            <Button label="No" icon="pi pi-times" text onClick={hideDeleteProdutosDialog} />
            <Button label="Yes" icon="pi pi-check" text onClick={deleteSelectedProdutos} />
        </>
    );

    return (
        <div className="grid crud-demo">
            <div className="col-12">
                <div className="card">
                    <Toast ref={toast} />
                    <Toolbar className="mb-4" left={leftToolbarTemplate} right={rightToolbarTemplate}></Toolbar>

                    <DataTable
                        ref={dt}
                        value={produtos}
                        selection={produtosSelecionados}
                        onSelectionChange={(e) => setProdutosSelecionados(e.value as any)}
                        dataKey="id"
                        paginator
                        rows={10}
                        rowsPerPageOptions={[5, 10, 25]}
                        className="datatable-responsive"
                        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                        currentPageReportTemplate="Mostrando {first} de {last} do total de {totalRecords} produtos"
                        filters={filters}
                        onFilter={(e) => setFilters(e.filters)}
                        globalFilterFields={["nomeProduto", "precoCustoProduto", "fornecedor", "dataCompra", "lucroPorProduto"]}
                        globalFilter={globalFilter}
                        emptyMessage="Nenhum produto encontrado."
                        header={header}
                        responsiveLayout="scroll"
                        onValueChange={(filtered) => {
                            // calcula o lucro só dos itens visíveis
                            const totalLucroFiltrado = (filtered || []).reduce(
                                (acc: number, produto: any) =>
                                    acc + ((produto.precoVendaProduto - produto.precoCustoProduto) * produto.quantidadeVendida || 0),
                                0
                            );
                            setLucro(totalLucroFiltrado);
                        }}
                    >
                        <Column selectionMode="multiple" headerStyle={{ width: '4rem' }}></Column>
                        {/* <Column field="id" header="Id" sortable body={idBodyTemplate}></Column> */}
                        <Column field="nomeProduto" header="Nome" sortable body={nomeBodyTemplate}></Column>
                        <Column field="fornecedor" header="Fornecedor" sortable body={fornecedorBodyTemplate}></Column>
                        <Column field="quantidadeEstoque" header="Quantidade em estoque" body={quantidadeEstoqueBodyTemplate} sortable></Column>
                        <Column field="precoCustoProduto" header="Preço de custo" body={precoCustoBodyTemplate} sortable></Column>
                        <Column field="quantidadeVendida" header="Quantidade vendida" body={quantidadeVendidaBodyTemplate} sortable></Column>
                        <Column field="precoVendaProduto" header="Preço de venda" body={precoVendaProdutoBodyTemplate} sortable></Column>
                        <Column
                            field="lucroPorProduto"
                            header="Lucro"
                            body={lucroPorProdutoBodyTemplate}
                            sortable
                        ></Column>
                        <Column field="dataCompra" header="Data de compra" body={dataCompraBodyTemplate} sortable headerStyle={{ minWidth: '10rem' }}></Column>
                        <Column body={actionBodyTemplate} headerStyle={{ minWidth: '10rem' }}></Column>
                    </DataTable>

                    <Dialog visible={produtoDialog} style={{ width: '450px' }} header="Detalhes do produto" modal className="p-fluid" footer={produtoDialogFooter} onHide={hideDialog}>
                        <div className="field">
                            <label htmlFor="nomeProduto">Nome</label>
                            <InputText
                                id="nomeProduto"
                                value={produto.nomeProduto}
                                onChange={(e) => onInputChange(e, 'nomeProduto')}
                                required
                                autoFocus
                                className={classNames({
                                    'p-invalid': submitted && !produto.nomeProduto
                                })}
                            />
                            {submitted && !produto.nomeProduto && <small className="p-invalid">Nome é obrigatório.</small>}
                        </div>

                        <div className="field">
                            <label htmlFor="fornecedor">Fornecedor</label>
                            <InputText
                                id="fornecedor"
                                value={produto.fornecedor}
                                onChange={(e) => onInputChange(e, 'fornecedor')}
                                required
                                autoFocus
                                className={classNames({
                                    'p-invalid': submitted && !produto.fornecedor
                                })}
                            />
                            {submitted && !produto.fornecedor && <small className="p-invalid" style={{ color: "#FCA5A5" }}>Fornecedor é obrigatório.</small>}
                        </div>

                        <div className="field">
                            <label htmlFor="quantidadeEstoque">Quantidade em estoque</label>
                            <InputNumber id="quantidadeEstoque" value={produto.quantidadeEstoque} onValueChange={(e) => onInputNumberChange(e, 'quantidadeEstoque')} />
                            {submitted && produto.quantidadeEstoque < 0 && <small className="p-invalid" style={{ color: "#FCA5A5" }}>Quantidade não pode ser negativa.</small>}
                        </div>

                        <div className="field">
                            <label htmlFor="precoCustoProduto">Preço de custo</label>
                            <InputNumber
                                id="precoCustoProduto"
                                value={produto.quantidadeEstoque}
                                onValueChange={(e) => onInputNumberChange(e, 'precoCustoProduto')}
                                mode="decimal"
                                minFractionDigits={0}
                                maxFractionDigits={2}
                            />
                            {submitted && produto.precoCustoProduto < 0 && <small className="p-invalid" style={{ color: "#FCA5A5" }}>Preço não pode ser negativo.</small>}
                        </div>

                        <div className="field">
                            <label htmlFor="quantidadeVendida">Quantidade vendida</label>
                            <InputNumber id="quantidadeVendida" value={produto.quantidadeVendida} onValueChange={(e) => onInputNumberChange(e, 'quantidadeVendida')} />
                            {submitted && produto.quantidadeVendida < 0 && <small className="p-invalid" style={{ color: "#FCA5A5" }}>Quantidade não pode ser negativa.</small>}
                            {submitted && produto.quantidadeVendida > produto.quantidadeEstoque && <small className="p-invalid" style={{ color: "#FCA5A5" }}>Quantidade vendida não pode ser maior que quantidade em estoque.</small>}
                        </div>

                        <div className="field">
                            <label htmlFor="precoVendaProduto">Preço de venda</label>
                            <InputNumber
                                id="precoVendaProduto"
                                value={produto.precoVendaProduto}
                                onValueChange={(e) => onInputNumberChange(e, 'precoVendaProduto')}
                                mode="decimal"
                                minFractionDigits={0}
                                maxFractionDigits={2}
                            />
                            {submitted && produto.precoVendaProduto < 0 && <small className="p-invalid" style={{ color: "#FCA5A5" }}>Preço de venda não pode ser negativo.</small>}
                            {submitted && produto.precoVendaProduto <= produto.precoCustoProduto && <small className="p-invalid" style={{ color: "#FCA5A5" }}>Preço de venda não pode ser menor ou igual ao preço de custo.</small>}
                        </div>

                        <div className="field">
                            <label htmlFor="dataCompra">Data de compra - Formato: ANO-MES-DIA</label>
                            <InputMask
                                id="dataCompra"
                                value={produto.dataCompra}
                                // fix any
                                onChange={(e: any) => onInputChange(e, 'dataCompra')}
                                mask="9999-99-99"
                                required
                                autoFocus
                                className={classNames({
                                    'p-invalid': submitted && !produto.dataCompra
                                })}
                            />
                            {submitted && checaFormatoDataInvalida(produto.dataCompra) && <small className="p-invalid" style={{ color: "#FCA5A5" }}>Formato da data incorreto ou data não existente.</small>}
                        </div>
                    </Dialog>

                    <Dialog visible={deleteProdutoDialog} style={{ width: '450px' }} header="Confirmar" modal footer={deleteProdutoDialogFooter} onHide={hideDeleteProdutoDialog}>
                        <div className="flex align-items-center justify-content-center">
                            <i className="pi pi-exclamation-triangle mr-3" style={{ fontSize: '2rem' }} />
                            {produto && (
                                <span>
                                    Tem certeza que deseja deletar <b>{produto.nomeProduto}</b>?
                                </span>
                            )}
                        </div>
                    </Dialog>

                    <Dialog visible={deleteProdutosDialog} style={{ width: '450px' }} header="Confirmar" modal footer={deleteProdutosDialogFooter} onHide={hideDeleteProdutosDialog}>
                        <div className="flex align-items-center justify-content-center">
                            <i className="pi pi-exclamation-triangle mr-3" style={{ fontSize: '2rem' }} />
                            {produto && <span>Tem certeza que deseja deletar os produtos selecionados?</span>}
                        </div>
                    </Dialog>
                </div>
            </div>
        </div>
    );
};

export default EstoquePage;
