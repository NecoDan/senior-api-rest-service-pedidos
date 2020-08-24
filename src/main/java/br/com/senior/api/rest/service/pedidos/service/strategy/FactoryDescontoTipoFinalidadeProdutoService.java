package br.com.senior.api.rest.service.pedidos.service.strategy;

import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.service.descontos.Desconto;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FactoryDescontoTipoFinalidadeProdutoService implements IFactoryDescontoService {

    @Override
    public boolean isAppliable(Pedido pedido) {
        return (Objects.nonNull(pedido) && isContemProdutosComTipoFinalidadeProdutoEmItensPedido(pedido));
    }

    @Override
    public Desconto obterDesconto(Pedido pedido) {
        return (isAppliable(pedido)) ? Desconto.DESCONTO_TIPO_PRODUTO : Desconto.SEM_DESCONTO;
    }

    private boolean isContemProdutosComTipoFinalidadeProdutoEmItensPedido(Pedido pedido) {
        return (pedido.isPermiteAplicarDesconto() && pedido.isExistemItensPermiteAplicarCalculoDesconto());
    }
}
