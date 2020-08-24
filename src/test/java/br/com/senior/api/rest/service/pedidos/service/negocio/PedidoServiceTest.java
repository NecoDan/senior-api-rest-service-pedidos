package br.com.senior.api.rest.service.pedidos.service.negocio;

import br.com.senior.api.rest.service.pedidos.model.pedido.ItemPedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.StatusPedido;
import br.com.senior.api.rest.service.pedidos.repository.IItemPedidoRepository;
import br.com.senior.api.rest.service.pedidos.repository.IPedidoRepository;
import br.com.senior.api.rest.service.pedidos.service.validation.PedidoValidationService;
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

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@Slf4j
public class PedidoServiceTest {

    @Mock
    private IPedidoRepository pedidoRepository;
    @Mock
    private IItemPedidoRepository itemPedidoRepository;
    @Mock
    private PedidoValidationService pedidoValidationService;
    @Spy
    @InjectMocks
    private PedidoService pedidoServiceMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private void resetarMocks() {
        reset(pedidoRepository);
        reset(itemPedidoRepository);
        reset(pedidoValidationService);
        reset(pedidoServiceMock);
    }

    @Test
    public void deveRecuperarPorIdUmUnicoPedido() {
        log.info("{} ", "#TEST: deveRecuperarPorIdUmUnicoPedido:");

        // -- 01_Cenário
        resetarMocks();
        UUID id = UUID.randomUUID();
        Pedido pedido = mock(Pedido.class);

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarPorId(any(UUID.class));
        when(pedidoServiceMock.recuperarPorId(id)).thenReturn(Optional.of(new Pedido()));

        // -- 03_Verificação_Validação
        assertTrue(pedidoServiceMock.recuperarPorId(id).isPresent());
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarTodosUmaListaPedidosPaginavel() {
        log.info("{} ", "#TEST: deveRecuperarTodosUmaListaPedidosPaginavel:");

        // -- 01_Cenário
        resetarMocks();
        Pageable pageable = PageRequest.of(0, 8);
        List<Pedido> pedidoList = Arrays.asList(mock(Pedido.class), mock(Pedido.class), mock(Pedido.class), mock(Pedido.class));
        Page<Pedido> pedidoPage = new PageImpl<>(pedidoList);

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarTodos(isA(Pageable.class));
        when(pedidoServiceMock.recuperarTodos(pageable)).thenReturn(pedidoPage);

        // -- 03_Verificação_Validação
        assertEquals(pedidoPage, pedidoServiceMock.recuperarTodos(pageable));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarTodosPedidosUmaListaPedidos() {
        log.info("{} ", "#TEST: deveRecuperarTodosPedidosUmaListaPedidos:");

        // -- 01_Cenário
        resetarMocks();
        List<Pedido> pedidoList = Arrays.asList(mock(Pedido.class), mock(Pedido.class), mock(Pedido.class));

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarTodosPedidos();
        when(pedidoServiceMock.recuperarTodosPedidos()).thenReturn(pedidoList);

        // -- 03_Verificação_Validação
        assertEquals(pedidoList, pedidoServiceMock.recuperarTodosPedidos());
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSalvarPedido() throws ServiceException {
        log.info("{} ", "#TEST: deveSalvarPedido: ");

        // -- 01_Cenário
        resetarMocks();
        Pedido pedidoParam = Pedido.builder().build();
        Pedido pedidoResult = mock(Pedido.class);

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).salvar(isA(Pedido.class));
        when(pedidoServiceMock.salvar(pedidoParam)).thenReturn(pedidoResult);
        pedidoServiceMock.salvar(pedidoParam);

        // -- 03_Verificação_Validação
        verify(pedidoServiceMock).salvar(any(Pedido.class));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSalvarItemPedido() throws ServiceException {
        log.info("{} ", "#TEST: deveSalvarItemPedido: ");

        // -- 01_Cenário
        resetarMocks();
        ItemPedido itemPedidoParam = ItemPedido.builder().build();
        ItemPedido pedidoResult = mock(ItemPedido.class);

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).salvarItem(isA(ItemPedido.class));
        when(pedidoServiceMock.salvarItem(itemPedidoParam)).thenReturn(pedidoResult);
        pedidoServiceMock.salvarItem(itemPedidoParam);

        // -- 03_Verificação_Validação
        verify(pedidoServiceMock).salvarItem(any(ItemPedido.class));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveAtualizarPedido() {
        log.info("{} ", "#TEST: deveSalvarPedido: ");

        // -- 01_Cenário
        resetarMocks();
        Pedido pedidoParam = Pedido.builder().build();
        Pedido pedidoResult = mock(Pedido.class);

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).atualizarPedido(isA(Pedido.class));
        when(pedidoServiceMock.atualizarPedido(pedidoParam)).thenReturn(pedidoResult);
        pedidoServiceMock.atualizarPedido(pedidoParam);

        // -- 03_Verificação_Validação
        verify(pedidoServiceMock).atualizarPedido(any(Pedido.class));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveTentarExcluirPedidoERetornarFalsePorNaoEncontrarPedido() throws ServiceException {
        log.info("{} ", "#TEST: deveTentarExcluirPedidoERetornarFalsePorNaoEncontrarPedido: ");

        // -- 01_Cenário
        resetarMocks();
        Pedido pedidoParam = Pedido.builder().build().gerarId();

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).excluir(isA(Pedido.class));
        boolean result = pedidoServiceMock.excluir(pedidoParam);

        // -- 03_Verificação_Validação
        assertFalse(result);
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarTodosPorTipoStatusPedidoUmaListaPedidos() throws ServiceException {
        log.info("{} ", "#TEST: deveRecuperarTodosPorTipoStatusPedidoUmaListaPedidos:");

        // -- 01_Cenário
        resetarMocks();
        StatusPedido statusPedido = StatusPedido.FECHADO_2;
        List<Pedido> pedidoList = Arrays.asList(mock(Pedido.class), mock(Pedido.class), mock(Pedido.class), mock(Pedido.class));

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarTodosPorStatus(isA(StatusPedido.class));
        when(pedidoServiceMock.recuperarTodosPorStatus(statusPedido)).thenReturn(pedidoList);

        // -- 03_Verificação_Validação
        assertEquals(pedidoList, pedidoServiceMock.recuperarTodosPorStatus(statusPedido));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarPedidosPorIntervaloDatas() throws ServiceException {
        log.info("{} ", "#TEST: deveRecuperarPedidosPorIntervaloDatas:");

        // -- 01_Cenário
        resetarMocks();
        LocalDate filtroDataInicial = LocalDate.now();
        LocalDate filtroDataFinal = LocalDate.now();
        List<Pedido> pedidoList = Arrays.asList(mock(Pedido.class), mock(Pedido.class), mock(Pedido.class), mock(Pedido.class), mock(Pedido.class), mock(Pedido.class));

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarTodosPorPeriodoDtCadastro(isA(LocalDate.class), isA(LocalDate.class));
        when(pedidoServiceMock.recuperarTodosPorPeriodoDtCadastro(filtroDataInicial, filtroDataFinal)).thenReturn(pedidoList);

        // -- 03_Verificação_Validação
        assertEquals(pedidoList, pedidoServiceMock.recuperarTodosPorPeriodoDtCadastro(filtroDataInicial, filtroDataFinal));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarPedidosPorFiltroDataCadastro() throws ServiceException {
        log.info("{} ", "#TEST: deveRecuperarPedidosPorFiltroDataCadastro:");

        // -- 01_Cenário
        resetarMocks();
        LocalDate filtroData = LocalDate.now();
        List<Pedido> pedidoList = Arrays.asList(mock(Pedido.class), mock(Pedido.class), mock(Pedido.class), mock(Pedido.class));

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarTodosPorDataCadastro(isA(LocalDate.class));
        when(pedidoServiceMock.recuperarTodosPorDataCadastro(filtroData)).thenReturn(pedidoList);

        // -- 03_Verificação_Validação
        assertEquals(pedidoList, pedidoServiceMock.recuperarTodosPorDataCadastro(filtroData));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveLancarExceptionAoRecuperarPedidosPorIntervaloDatas() throws ServiceException {
        log.info("{} ", "#TEST: deveLancarExceptionAoRecuperarPedidosPorIntervaloDatas: ");

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarTodosPorPeriodoDtCadastro(isA(LocalDate.class), isA(LocalDate.class));
        ServiceException exception = assertThrows(ServiceException.class,
                () -> pedidoServiceMock.recuperarTodosPorPeriodoDtCadastro(LocalDate.now(), null));

        // -- 03_Verificação_Validação
        assertTrue(exception.getMessage().contains("contendo as datas"));
        log.info("{} ", "EXCEPTION: ".concat(exception.getMessage()));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveLancarExceptionAoRecuperarPedidosPorDataCadastro() throws ServiceException {
        log.info("{} ", "#TEST: deveLancarExceptionAoRecuperarPedidosPorDataCadastro: ");

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarTodosPorDataCadastro(isA(LocalDate.class));
        ServiceException exception = assertThrows(ServiceException.class,
                () -> pedidoServiceMock.recuperarTodosPorDataCadastro(null));

        // -- 03_Verificação_Validação
        assertTrue(exception.getMessage().contains("contendo a data"));
        log.info("{} ", "EXCEPTION: ".concat(exception.getMessage()));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveLancarExceptionAoExcluirPedido() throws ServiceException {
        log.info("{} ", "#TEST: deveLancarExceptionAoExcluirPedido: ");

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(pedidoServiceMock).excluir(isA(Pedido.class));
        ServiceException exception = assertThrows(ServiceException.class,
                () -> pedidoServiceMock.excluir(Pedido.builder().build()));

        // -- 03_Verificação_Validação
        assertTrue(exception.getMessage().contains("{ID} referente ao Pedido"));
        log.info("{} ", "EXCEPTION: ".concat(exception.getMessage()));
        log.info("{} ", "-------------------------------------------------------------");
    }
}
