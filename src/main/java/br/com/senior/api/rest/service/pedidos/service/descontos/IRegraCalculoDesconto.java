package br.com.senior.api.rest.service.pedidos.service.descontos;


import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface IRegraCalculoDesconto {

    BigDecimal calculaDesconto(Pedido pedido);

    double calcularDesconto(Pedido pedido);
}
