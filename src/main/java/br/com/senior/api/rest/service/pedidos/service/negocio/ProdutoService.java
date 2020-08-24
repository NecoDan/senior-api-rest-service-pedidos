package br.com.senior.api.rest.service.pedidos.service.negocio;


import br.com.senior.api.rest.service.pedidos.model.cadastro.Produto;
import br.com.senior.api.rest.service.pedidos.model.cadastro.TipoFinalidadeProduto;
import br.com.senior.api.rest.service.pedidos.repository.IProdutoRepository;
import br.com.senior.api.rest.service.pedidos.service.validation.IProdutoValidationService;
import br.com.senior.api.rest.service.pedidos.util.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProdutoService implements IProdutoService {

    private final IProdutoRepository produtoRepository;
    private final IProdutoValidationService produtoValidationService;

    @Override
    public Optional<Produto> recuperarPorId(UUID id) {
        return this.produtoRepository.findById(id);
    }

    @Override
    public Page<Produto> recuperarTodos(Pageable pageable) {
        return this.produtoRepository.findAll(pageable);
    }

    @Override
    public List<Produto> recuperarTodosProdutos() {
        return this.produtoRepository.findAll();
    }

    @Override
    public Page<Produto> recuperarAtivos(Pageable pageable) {
        return this.produtoRepository.findAllByAtivo(true, pageable);
    }

    @Override
    public Page<Produto> recuperarInativos(Pageable pageable) {
        return this.produtoRepository.findAllByAtivo(false, pageable);
    }

    @Override
    public List<Produto> recuperarTodosPorTipoFinalidade(TipoFinalidadeProduto tipoFinalidadeProduto) throws ServiceException {
        if (Objects.isNull(tipoFinalidadeProduto))
            throw new ServiceException("Filtro contendo o tipo de finalidade do produto para efetuar a busca do Produtos, encontra-se inválido e/ou inexistente {NULL}.");
        return this.produtoRepository.recuperarTodosPorTipoFinalidade(tipoFinalidadeProduto);
    }

    @Override
    public List<Produto> recuperarPorDescricao(String descricao) throws ServiceException {
        if (Objects.isNull(descricao) || descricao.isEmpty())
            throw new ServiceException("Filtro contendo a descrição para efetuar a busca do Produtos, encontra-se inválida e/ou conteudo vazia.");
        return this.produtoRepository.findAllByDescricaoContainingIgnoreCase(descricao);
    }

    @Override
    public List<Produto> recuperarTodosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ServiceException {
        if (Objects.isNull(dataInicio) || Objects.isNull(dataFim))
            throw new ServiceException("Filtro contendo as datas para efeutar a busca do(s) Produtos(s), encontra-se inválida e/ou inexistente {NULL}.");
        return this.produtoRepository.recuperarTodosPorPeriodoDtCadastro(dataInicio, dataFim);
    }

    @Override
    public List<Produto> recuperarPorDataCadastro(LocalDate data) throws ServiceException {
        if (Objects.isNull(data))
            throw new ServiceException("Filtro contendo a data para efeutar a busca do(s) Produtos(s), encontra-se inválida e/ou inexistente {NULL}.");
        return this.produtoRepository.recuperarTodosPorDataCadastro(data);
    }

    @Override
    @Transactional
    public Produto salvar(Produto produto) throws ServiceException {
        produto.geraId();
        produto.gerarCodigoProdutoAutomatico();
        produto.gerarCodigoBarrasAutomatico();
        produto.gerarDataCorrente();
        produto.gerarValorCustoAutomatico();
        produto.ativado();
        this.produtoValidationService.validarProduto(produto);
        return this.produtoRepository.saveAndFlush(produto);
    }

    @Override
    @Transactional
    public Produto atualizar(UUID produtoId, Produto produto) throws ServiceException {
        if (Objects.isNull(produtoId))
            throw new ServiceException("{ID} referente ao Produto encontra-se inválido e/ou inexistente {NULL}.");

        Produto produtoAtualizar = recuperarPorId(produtoId)
                .map(p -> {
                    p.setCodigoProduto((Objects.isNull(produto.getCodigoProduto()) || produto.getCodigoProduto() <= 0) ? p.getCodigoProduto() : produto.getCodigoProduto());
                    p.setCodigoBarras((Objects.isNull(produto.getCodigoBarras()) || produto.getCodigoBarras().isEmpty()) ? p.getCodigoBarras() : produto.getCodigoBarras());
                    p.setTipoFinalidadeProduto(Objects.isNull(produto.getTipoFinalidadeProduto()) ? p.getTipoFinalidadeProduto() : produto.getTipoFinalidadeProduto());
                    p.setDescricao((Objects.isNull(produto.getDescricao()) || produto.getDescricao().isEmpty()) ? p.getDescricao() : produto.getDescricao());
                    p.setAtivo(produto.isAtivo());
                    p.gerarDataCorrente();
                    p.tratarValorCustoProdutoPor(produto);
                    return p;
                }).orElse(null);

        this.produtoRepository.save(Objects.requireNonNull(produtoAtualizar));
        return produtoAtualizar;
    }

    @Override
    @Transactional
    public boolean excluir(Produto produto) throws ServiceException {
        return (!Objects.isNull(produto)) && excluirPor(produto.getId());
    }

    @Override
    public boolean excluirPor(UUID id) throws ServiceException {
        if (Objects.isNull(id))
            throw new ServiceException("{ID} referente ao Produto encontra-se inválido e/ou inexistente {NULL}.");

        validarProdutoPermiteExclusao(id);

        return recuperarPorId(id)
                .map(prod -> {
                    this.produtoRepository.deleteById(prod.getId());
                    return true;
                }).orElse(false);
    }

    @Override
    public void validarProdutoPermiteExclusao(UUID id) throws ServiceException {
        if (isProdutoNaoPossuiPermissaoASerExcluido(id)) {
            String msgValidacao = "Falha de negócio! Não é possível excluir o Produto. Existem pedidos registrados e com movimentos referente ao Produto informado: ";

            Optional<Produto> optionalProduto = recuperarPorId(id);

            if (optionalProduto.isPresent())
                msgValidacao += optionalProduto.get().toStringProduto();

            throw new ServiceException(msgValidacao);
        }
    }

    private boolean isProdutoNaoPossuiPermissaoASerExcluido(UUID idProduto) {
        return Objects.equals(true, this.produtoRepository.isProdutoPossuiMovimentosEmPedidos(idProduto));
    }
}
