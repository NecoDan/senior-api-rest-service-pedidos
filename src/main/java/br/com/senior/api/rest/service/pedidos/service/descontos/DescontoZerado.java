package br.com.senior.api.rest.service.pedidos.service.descontos;

import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DescontoZerado implements IRegraCalculoDesconto {

    @Override
    public BigDecimal calculaDesconto(Pedido pedido) {
        return BigDecimal.ZERO;
    }

    @Override
    public double calcularDesconto(Pedido pedido) {
        return calculaDesconto(pedido).doubleValue();
    }
}
