package br.com.senior.api.rest.service.pedidos.service.gerador;

import br.com.senior.api.rest.service.pedidos.model.pedido.ItemPedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.util.exceptions.ServiceException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface IGeraPedidoService {

    void validarProdutosItensPedido(List<ItemPedido> itemPedidoList) throws ServiceException;

    @Transactional(value = Transactional.TxType.REQUIRED)
    Pedido atualizarPedido(UUID idPedido, Pedido pedido) throws ServiceException;

    @Transactional(value = Transactional.TxType.REQUIRED)
    Pedido gerar(Pedido pedido) throws ServiceException;

    @Transactional(value = Transactional.TxType.REQUIRED)
    void efetuarGeracaoItensPedido(Pedido pedido, List<ItemPedido> itens) throws ServiceException;

    @Transactional(value = Transactional.TxType.REQUIRED)
    void efetuarGeracaoPedido(Pedido pedidoNovo, boolean calculaDesconto) throws ServiceException;

    @Transactional(value = Transactional.TxType.REQUIRED)
    ItemPedido gerarItemPedido(Integer item, ItemPedido itemPedido) throws ServiceException;
}
