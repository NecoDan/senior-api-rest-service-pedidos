package br.com.senior.api.rest.service.pedidos.service.strategy;

import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.service.descontos.Desconto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BuilderDescontoStrategy {

    private final List<IFactoryDescontoService> factoryDescontoServicesList;

    public BuilderDescontoStrategy(List<IFactoryDescontoService> factoryDescontoServicesList) {
        this.factoryDescontoServicesList = factoryDescontoServicesList;
    }

    public Desconto obter(Pedido pedido) {
        Optional<Desconto> optionalDesconto = this.factoryDescontoServicesList
                .stream()
                .filter(Objects::nonNull)
                .filter(factoryTipoLayoutArquivoService -> factoryTipoLayoutArquivoService.isAppliable(pedido))
                .map(factoryTipoLayoutArquivoService -> factoryTipoLayoutArquivoService.obterDesconto(pedido))
                .findFirst();
        return optionalDesconto.orElse(Desconto.SEM_DESCONTO);
    }
}
