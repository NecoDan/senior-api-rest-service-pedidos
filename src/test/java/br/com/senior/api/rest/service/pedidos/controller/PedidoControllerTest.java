package br.com.senior.api.rest.service.pedidos.controller;

import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.StatusPedido;
import br.com.senior.api.rest.service.pedidos.util.uteis.RandomicoUtil;
import org.junit.Before;

import static org.junit.Assert.*;

public class PedidoControllerTest {

    @Before
    public void setUp() throws Exception {
    }

    private Pedido constroiPedidoValido() {
        Pedido pedido = Pedido.builder()
                .statusPedido(StatusPedido.ABERTO_1)
                .valorItens(RandomicoUtil.gerarValorRandomicoDecimal())
                .valorTotal(RandomicoUtil.gerarValorRandomicoDecimal())
                .valorPercentualDesconto(RandomicoUtil.gerarValorRandomicoDecimal())
                .build()
                .gerarId();

        pedido.ativado();
        pedido.gerarDataCorrente();
        return pedido;
    }
}
