package br.com.senior.api.rest.service.pedidos.service.descontos;

import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class DescontoTipoFinalidadeProduto implements IRegraCalculoDesconto {

    @Override
    public BigDecimal calculaDesconto(Pedido pedido) {
        return (isParamsPedidoInValidos(pedido)) ? BigDecimal.ZERO : efetuarCalculoDesconto(pedido);
    }

    @Override
    public double calcularDesconto(Pedido pedido) {
        return calculaDesconto(pedido).doubleValue();
    }

    private boolean isParamsPedidoInValidos(Pedido pedido) {
        return (Objects.isNull(pedido) || Objects.isNull(pedido.getValorTotal()) || Objects.isNull(pedido.getValorPercentualDesconto()));
    }

    private BigDecimal efetuarCalculoDesconto(Pedido pedido) {
        return pedido.getValorTotal().multiply(BigDecimal.valueOf(pedido.getValorPercentualDesconto().doubleValue() / 100)).setScale(2, RoundingMode.HALF_EVEN);
    }
}
