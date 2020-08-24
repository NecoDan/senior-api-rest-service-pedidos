package br.com.senior.api.rest.service.pedidos.model.pedido;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public enum StatusPedido {

    NENHUM_0(0, "0", "0 - NENHUM"),

    ABERTO_1(1, "A", "1 - ABERTO"),

    FECHADO_2(2, "F", "2 - FECHADO");

    private static final Map<Integer, StatusPedido> lookup;

    static {
        lookup = new HashMap<>();
        EnumSet<StatusPedido> enumSet = EnumSet.allOf(StatusPedido.class);

        for (StatusPedido type : enumSet)
            lookup.put(type.codigo, type);
    }

    private int codigo;
    private String codigoLiteral;
    private String descricao;

    StatusPedido(int codigo, String codigoLiteral, String descricao) {
        inicialize(codigo, codigoLiteral, descricao);
    }

    private void inicialize(int codigo, String codigoLiteral, String descricao) {
        this.codigo = codigo;
        this.codigoLiteral = codigoLiteral;
        this.descricao = descricao;
    }

    public static StatusPedido fromCodigo(int codigo) {
        if (lookup.containsKey(codigo))
            return lookup.get(codigo);
        throw new IllegalArgumentException(String.format("Código do Status do Pedido encontra-se inválido: %d", codigo));
    }

    public static StatusPedido of(int codigo) {
        return Stream.of(StatusPedido.values())
                .filter(p -> p.getCodigo() == codigo)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int getCodigo() {
        return this.codigo;
    }

    public String getCodigoLiteral() {
        return this.codigoLiteral;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public boolean isAberto() {
        return Objects.equals(this, ABERTO_1);
    }

    public boolean isFechado() {
        return Objects.equals(this, FECHADO_2);
    }
}
