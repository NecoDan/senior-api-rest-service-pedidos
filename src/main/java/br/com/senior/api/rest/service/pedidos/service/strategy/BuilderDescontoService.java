package br.com.senior.api.rest.service.pedidos.service.strategy;

import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.service.descontos.Desconto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BuilderDescontoService implements IBuilderDescontoService {

    private final BuilderDescontoStrategy builderDescontoStrategy;

    @Override
    public Desconto obterDescontoAPartir(Pedido pedido) {
        return this.builderDescontoStrategy.obter(pedido);
    }
}
