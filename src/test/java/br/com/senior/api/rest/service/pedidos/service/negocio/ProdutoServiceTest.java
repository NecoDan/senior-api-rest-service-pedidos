package br.com.senior.api.rest.service.pedidos.service.negocio;


import br.com.senior.api.rest.service.pedidos.model.cadastro.Produto;
import br.com.senior.api.rest.service.pedidos.model.cadastro.TipoFinalidadeProduto;
import br.com.senior.api.rest.service.pedidos.repository.IProdutoRepository;
import br.com.senior.api.rest.service.pedidos.service.validation.ProdutoValidationService;
import br.com.senior.api.rest.service.pedidos.util.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
public class ProdutoServiceTest {

    @Mock
    private IProdutoRepository produtoRepository;
    @Mock
    private ProdutoValidationService produtoValidationService;
    @Spy
    @InjectMocks
    private ProdutoService produtoServiceMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private void resetarMocks() {
        reset(produtoRepository);
        reset(produtoValidationService);
        reset(produtoServiceMock);
    }

    @Test
    public void deveRecuperarPorIdUmUnicoProduto() {
        log.info("{} ", "#TEST: deveRecuperarTodosProdutosUmaListaDeProduto:");

        // -- 01_Cenário
        resetarMocks();
        UUID id = UUID.randomUUID();
        Produto produto = mock(Produto.class);

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(produtoServiceMock).recuperarPorId(any(UUID.class));
        when(produtoServiceMock.recuperarPorId(id)).thenReturn(Optional.of(new Produto()));

        // -- 03_Verificação_Validação
        assertTrue(produtoServiceMock.recuperarPorId(id).isPresent());
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarTodosUmaListaDeProdutosPaginavel() {
        log.info("{} ", "#TEST: deveRecuperarTodosUmaListaDeProdutosPaginavel:");

        // -- 01_Cenário
        resetarMocks();
        Pageable pageable = PageRequest.of(0, 8);
        List<Produto> produtoList = Arrays.asList(mock(Produto.class), mock(Produto.class), mock(Produto.class), mock(Produto.class));
        Page<Produto> produtoPage = new PageImpl<>(produtoList);

        // -- 02_Ação
        doCallRealMethod().when(produtoServiceMock).recuperarTodos(isA(Pageable.class));
        when(produtoServiceMock.recuperarTodos(pageable)).thenReturn(produtoPage);

        // -- 03_Verificação_Validação
        assertEquals(produtoPage, produtoServiceMock.recuperarTodos(pageable));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarTodosProdutosUmaListaDeProduto() {
        log.info("{} ", "#TEST: deveRecuperarTodosProdutosUmaListaDeProduto:");

        // -- 01_Cenário
        resetarMocks();
        List<Produto> produtoList = Arrays.asList(mock(Produto.class), mock(Produto.class), mock(Produto.class));

        // -- 02_Ação
        doCallRealMethod().when(produtoServiceMock).recuperarTodosProdutos();
        when(produtoServiceMock.recuperarTodosProdutos()).thenReturn(produtoList);

        // -- 03_Verificação_Validação
        assertEquals(produtoList, produtoServiceMock.recuperarTodosProdutos());
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void recuperarAtivosUmaListaProduto() {
        log.info("{} ", "#TEST: recuperarAtivosUmaListaProduto:");

        // -- 01_Cenário
        resetarMocks();
        Pageable pageable = PageRequest.of(0, 8);
        List<Produto> produtoListAtivos = Arrays.asList(mock(Produto.class), mock(Produto.class), mock(Produto.class), mock(Produto.class));
        Page<Produto> pageProdutosAtivos = new PageImpl<>(produtoListAtivos);

        // -- 02_Ação
        doCallRealMethod().when(produtoServiceMock).recuperarAtivos(isA(Pageable.class));
        when(produtoServiceMock.recuperarAtivos(pageable)).thenReturn(pageProdutosAtivos);

        // -- 03_Verificação_Validação
        assertEquals(pageProdutosAtivos, produtoServiceMock.recuperarAtivos(pageable));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void recuperarInativos() {
        log.info("{} ", "#TEST: recuperarAtivosUmaListaProduto:");

        // -- 01_Cenário
        resetarMocks();
        Pageable pageable = PageRequest.of(0, 8);
        List<Produto> produtoListInativos = Arrays.asList(mock(Produto.class), mock(Produto.class), mock(Produto.class), mock(Produto.class));
        Page<Produto> pageProdutosInativos = new PageImpl<>(produtoListInativos);

        // -- 02_Ação
        doCallRealMethod().when(produtoServiceMock).recuperarInativos(isA(Pageable.class));
        when(produtoServiceMock.recuperarInativos(pageable)).thenReturn(pageProdutosInativos);

        // -- 03_Verificação_Validação
        assertEquals(pageProdutosInativos, produtoServiceMock.recuperarInativos(pageable));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarTodosPorTipoFinalidadeUmaListaProdutos() throws ServiceException {
        log.info("{} ", "#TEST: deveRecuperarTodosPorTipoFinalidadeUmaListaProdutos:");

        // -- 01_Cenário
        resetarMocks();
        TipoFinalidadeProduto tipoFinalidadeProduto = TipoFinalidadeProduto.SERVICO_2;
        List<Produto> produtoList = Arrays.asList(mock(Produto.class), mock(Produto.class), mock(Produto.class), mock(Produto.class));

        // -- 02_Ação
        doCallRealMethod().when(produtoServiceMock).recuperarTodosPorTipoFinalidade(isA(TipoFinalidadeProduto.class));
        when(produtoServiceMock.recuperarTodosPorTipoFinalidade(tipoFinalidadeProduto)).thenReturn(produtoList);

        // -- 03_Verificação_Validação
        assertEquals(produtoList, produtoServiceMock.recuperarTodosPorTipoFinalidade(tipoFinalidadeProduto));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarPorDescricaoUmaListaProdutos() throws ServiceException {
        log.info("{} ", "#TEST: deveRecuperarPorDescricaoUmaListaProdutos:");

        // -- 01_Cenário
        resetarMocks();
        String descricao = "Cadeiras";
        List<Produto> produtoList = Arrays.asList(mock(Produto.class), mock(Produto.class), mock(Produto.class), mock(Produto.class));

        // -- 02_Ação
        doCallRealMethod().when(produtoServiceMock).recuperarPorDescricao(isA(String.class));
        when(produtoServiceMock.recuperarPorDescricao(descricao)).thenReturn(produtoList);

        // -- 03_Verificação_Validação
        assertEquals(produtoList, produtoServiceMock.recuperarPorDescricao(descricao));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarTodosPorPeriodoUmaListaDeProdutos() throws ServiceException {
        log.info("{} ", "#TEST: deveRecuperarTodosPorPeriodoUmaListaDeProdutos:");

        // -- 01_Cenário
        resetarMocks();
        LocalDate filtroDataInicial = LocalDate.now();
        LocalDate filtroDataFinal = LocalDate.now();
        List<Produto> produtoList = Arrays.asList(mock(Produto.class), mock(Produto.class), mock(Produto.class), mock(Produto.class), mock(Produto.class), mock(Produto.class));

        // -- 02_Ação
        doCallRealMethod().when(produtoServiceMock).recuperarTodosPorPeriodo(isA(LocalDate.class), isA(LocalDate.class));
        when(produtoServiceMock.recuperarTodosPorPeriodo(filtroDataInicial, filtroDataFinal)).thenReturn(produtoList);

        // -- 03_Verificação_Validação
        assertEquals(produtoList, produtoServiceMock.recuperarTodosPorPeriodo(filtroDataInicial, filtroDataFinal));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarPorDataCadastroUmaListaDeProdutos() throws ServiceException {
        log.info("{} ", "#TEST: deveRecuperarPorDataCadastroUmaListaDeProdutos:");

        // -- 01_Cenário
        resetarMocks();
        LocalDate filtroData = LocalDate.now();
        List<Produto> produtoList = Arrays.asList(mock(Produto.class), mock(Produto.class), mock(Produto.class), mock(Produto.class));

        // -- 02_Ação
        doCallRealMethod().when(produtoServiceMock).recuperarPorDataCadastro(isA(LocalDate.class));
        when(produtoServiceMock.recuperarPorDataCadastro(filtroData)).thenReturn(produtoList);

        // -- 03_Verificação_Validação
        assertEquals(produtoList, produtoServiceMock.recuperarPorDataCadastro(filtroData));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveLancarExceptionAoRecuperarTodosPorTipoFinalidade() throws ServiceException {
        log.info("{} ", "#TEST: deveLancarExceptionAoRecuperarTodosPorTipoFinalidade: ");

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(produtoServiceMock).recuperarTodosPorTipoFinalidade(isA(TipoFinalidadeProduto.class));
        ServiceException exception = assertThrows(ServiceException.class,
                () -> produtoServiceMock.recuperarTodosPorTipoFinalidade(null));

        // -- 03_Verificação_Validação
        assertTrue(exception.getMessage().contains("o tipo de finalidade do produto"));
        log.info("{} ", "EXCEPTION: ".concat(exception.getMessage()));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveLancarExceptionAoRecuperarPorDescricao() throws ServiceException {
        log.info("{} ", "#TEST: deveLancarExceptionAoRecuperarPorDescricao: ");

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(produtoServiceMock).recuperarPorDescricao(isA(String.class));
        ServiceException exception = assertThrows(ServiceException.class,
                () -> produtoServiceMock.recuperarPorDescricao(null));

        // -- 03_Verificação_Validação
        assertTrue(exception.getMessage().contains("contendo a descrição para efetuar"));
        log.info("{} ", "EXCEPTION: ".concat(exception.getMessage()));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveLancarExceptionAoRecuperarTodosPorPeriodo() throws ServiceException {
        log.info("{} ", "#TEST: deveLancarExceptionAoRecuperarTodosPorPeriodo: ");

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(produtoServiceMock).recuperarTodosPorPeriodo(isA(LocalDate.class), isA(LocalDate.class));
        ServiceException exception = assertThrows(ServiceException.class,
                () -> produtoServiceMock.recuperarTodosPorPeriodo(LocalDate.now(), null));

        // -- 03_Verificação_Validação
        assertTrue(exception.getMessage().contains("contendo as datas"));
        log.info("{} ", "EXCEPTION: ".concat(exception.getMessage()));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveLancarExceptionAoRecuperarPorDataCadastro() throws ServiceException {
        log.info("{} ", "#TEST: deveLancarExceptionAoRecuperarPorDataCadastro: ");

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(produtoServiceMock).recuperarPorDataCadastro(isA(LocalDate.class));
        ServiceException exception = assertThrows(ServiceException.class,
                () -> produtoServiceMock.recuperarPorDataCadastro(null));

        // -- 03_Verificação_Validação
        assertTrue(exception.getMessage().contains("contendo a data"));
        log.info("{} ", "EXCEPTION: ".concat(exception.getMessage()));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveLancarExceptionAoExcluirProduto() throws ServiceException {
        log.info("{} ", "#TEST: deveLancarExceptionAoExcluirProduto: ");

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(produtoServiceMock).excluir(isA(Produto.class));
        ServiceException exception = assertThrows(ServiceException.class,
                () -> produtoServiceMock.excluir(Produto.builder().build()));

        // -- 03_Verificação_Validação
        assertTrue(exception.getMessage().contains("{ID} referente ao Produto"));
        log.info("{} ", "EXCEPTION: ".concat(exception.getMessage()));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void salvarProduto() {
    }

    @Test
    public void deveExcluirProduto() {

    }

}
