package br.com.senior.api.rest.service.pedidos.service.validation;

import br.com.senior.api.rest.service.pedidos.model.pedido.ItemPedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.util.exceptions.ServiceException;

public interface IPedidoValidationService {

    void validarSomentePedido(Pedido pedido) throws ServiceException;

    void validarIdReferentePedido(Pedido pedido) throws ServiceException;

    void validarSomenteItemPedido(ItemPedido itemPedido) throws ServiceException;

    void validarIdReferenteItemPedido(ItemPedido itemPedido) throws ServiceException;

    void validarParamsEDependenciasPedido(Pedido pedido) throws ServiceException;
}
