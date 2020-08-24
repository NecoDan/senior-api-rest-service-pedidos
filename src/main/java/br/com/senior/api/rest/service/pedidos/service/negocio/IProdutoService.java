package br.com.senior.api.rest.service.pedidos.service.negocio;

import br.com.senior.api.rest.service.pedidos.model.cadastro.Produto;
import br.com.senior.api.rest.service.pedidos.model.cadastro.TipoFinalidadeProduto;
import br.com.senior.api.rest.service.pedidos.util.exceptions.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProdutoService {

    Optional<Produto> recuperarPorId(UUID id);

    Page<Produto> recuperarTodos(Pageable pageable);

    List<Produto> recuperarTodosProdutos();

    Page<Produto> recuperarAtivos(Pageable pageable);

    Page<Produto> recuperarInativos(Pageable pageable);

    List<Produto> recuperarTodosPorTipoFinalidade(TipoFinalidadeProduto tipoFinalidadeProduto) throws ServiceException;

    List<Produto> recuperarPorDescricao(String descricao) throws ServiceException;

    List<Produto> recuperarTodosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ServiceException;

    List<Produto> recuperarPorDataCadastro(LocalDate data) throws ServiceException;

    @Transactional
    Produto salvar(Produto produto) throws ServiceException;

    @Transactional
    Produto atualizar(UUID produtoId, Produto produto) throws ServiceException;

    @Transactional
    boolean excluir(Produto produto) throws ServiceException;

    @Transactional
    boolean excluirPor(UUID id) throws ServiceException;

    void validarProdutoPermiteExclusao(UUID id) throws ServiceException;
}
