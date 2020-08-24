package br.com.senior.api.rest.service.pedidos.service.strategy;

import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.service.descontos.Desconto;

public interface IFactoryDescontoService {
    boolean isAppliable(Pedido pedido);

    Desconto obterDesconto(Pedido pedido);
}
