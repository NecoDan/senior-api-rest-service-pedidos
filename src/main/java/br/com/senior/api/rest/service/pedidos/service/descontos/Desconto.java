package br.com.senior.api.rest.service.pedidos.service.descontos;

public enum Desconto {

    SEM_DESCONTO(new DescontoZerado()),

    DESCONTO_TIPO_PRODUTO(new DescontoTipoFinalidadeProduto());

    private final IRegraCalculoDesconto regraCalculoPromocaoDesconto;

    Desconto(IRegraCalculoDesconto regraCalculoPromocaoDesconto) {
        this.regraCalculoPromocaoDesconto = regraCalculoPromocaoDesconto;
    }

    public IRegraCalculoDesconto getRegraCalculoPromocaoDesconto() {
        return this.regraCalculoPromocaoDesconto;
    }
}
