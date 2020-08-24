package br.com.senior.api.rest.service.pedidos.service.negocio;

import br.com.senior.api.rest.service.pedidos.model.pedido.ItemPedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.StatusPedido;
import br.com.senior.api.rest.service.pedidos.util.exceptions.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPedidoService {

    Optional<Pedido> recuperarPorId(UUID id);

    Page<Pedido> recuperarTodos(Pageable pageable);

    List<Pedido> recuperarTodosPedidos();

    List<Pedido> recuperarTodosPorStatus(StatusPedido statusPedido) throws ServiceException;

    List<Pedido> recuperarTodosPorDataCadastro(LocalDate data) throws ServiceException;

    List<Pedido> recuperarTodosPorPeriodoDtCadastro(LocalDate dataInicio, LocalDate dataFim) throws ServiceException;

    @Transactional
    Pedido salvar(Pedido pedido) throws ServiceException;

    @Transactional
    boolean excluir(Pedido pedido) throws ServiceException;

    @Transactional
    boolean excluirPor(UUID id) throws ServiceException;

    @Transactional(value = Transactional.TxType.REQUIRED)
    Pedido atualizarPedido(Pedido pedido) throws ServiceException;

    @Transactional
    ItemPedido salvarItem(ItemPedido itemPedido) throws ServiceException;
}
