package br.com.senior.api.rest.service.pedidos.service.negocio;

import br.com.senior.api.rest.service.pedidos.model.pedido.ItemPedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.StatusPedido;
import br.com.senior.api.rest.service.pedidos.repository.IItemPedidoRepository;
import br.com.senior.api.rest.service.pedidos.repository.IPedidoRepository;
import br.com.senior.api.rest.service.pedidos.service.validation.IPedidoValidationService;
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
public class PedidoService implements IPedidoService {

    private final IPedidoRepository pedidoRepository;
    private final IPedidoValidationService pedidoValidationService;
    private final IItemPedidoRepository itemPedidoRepository;

    @Override
    public Optional<Pedido> recuperarPorId(UUID id) {
        return this.pedidoRepository.findById(id);
    }

    @Override
    public Page<Pedido> recuperarTodos(Pageable pageable) {
        return this.pedidoRepository.findAll(pageable);
    }

    @Override
    public List<Pedido> recuperarTodosPedidos() {
        return this.pedidoRepository.findAll();
    }

    @Override
    public List<Pedido> recuperarTodosPorStatus(StatusPedido statusPedido) throws ServiceException {
        if (Objects.isNull(statusPedido))
            throw new ServiceException("Filtro contendo o status do pedido para efetuar a busca do Pedidos(s), encontra-se inválido e/ou inexistente {NULL}.");
        return this.pedidoRepository.recuperarTodosPorStatusPedido(statusPedido);
    }

    @Override
    public List<Pedido> recuperarTodosPorDataCadastro(LocalDate data) throws ServiceException {
        if (Objects.isNull(data))
            throw new ServiceException("Filtro contendo a data para efeutar a busca do(s) Pedidos(s), encontra-se inválida e/ou inexistente {NULL}.");
        return this.pedidoRepository.recuperarTodosPorDataCadastro(data);
    }

    @Override
    public List<Pedido> recuperarTodosPorPeriodoDtCadastro(LocalDate dataInicio, LocalDate dataFim) throws ServiceException {
        if (Objects.isNull(dataInicio) || Objects.isNull(dataFim))
            throw new ServiceException("Filtro contendo as datas para efeutar a busca do(s) Pedidos(s), encontra-se inválida e/ou inexistente {NULL}.");
        return this.pedidoRepository.recuperarTodosPorPeriodoDtCadastro(dataInicio, dataFim);
    }

    @Override
    @Transactional
    public Pedido salvar(Pedido pedido) throws ServiceException {
        this.pedidoValidationService.validarSomentePedido(pedido);
        return this.pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public boolean excluir(Pedido pedido) throws ServiceException {
        return (Objects.nonNull(pedido)) && excluirPor(pedido.getId());
    }

    @Transactional
    @Override
    public boolean excluirPor(UUID id) throws ServiceException {
        if (Objects.isNull(id))
            throw new ServiceException("{ID} referente ao Pedido, encontra-se inválido e/ou inexistente {NULL}.");

        return recuperarPorId(id)
                .map(prod -> {
                    prod.getItens().forEach(this.itemPedidoRepository::delete);
                    this.pedidoRepository.deleteById(prod.getId());
                    return true;
                }).orElse(false);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public Pedido atualizarPedido(Pedido pedido) {
        if (Objects.nonNull(pedido))
            this.pedidoRepository.saveAndFlush(pedido);
        return pedido;
    }

    @Override
    @Transactional
    public ItemPedido salvarItem(ItemPedido itemPedido) throws ServiceException {
        this.pedidoValidationService.validarSomenteItemPedido(itemPedido);
        return this.itemPedidoRepository.save(itemPedido);
    }
}
