package br.com.senior.api.rest.service.pedidos.model.cadastro;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public enum TipoFinalidadeProduto {

    NENHUM_0(0, "000", "NENHUM"),

    PRODUTO_1(1, "001", "PRODUTO"),

    SERVICO_2(2, "002", "SERVIÇO");

    private static final Map<Integer, TipoFinalidadeProduto> lookup;

    static {
        lookup = new HashMap<>();
        EnumSet<TipoFinalidadeProduto> enumSet = EnumSet.allOf(TipoFinalidadeProduto.class);

        for (TipoFinalidadeProduto type : enumSet)
            lookup.put(type.codigo, type);
    }

    private int codigo;
    private String codigoLiteral;
    private String descricao;

    TipoFinalidadeProduto(int codigo, String codigoLiteral, String descricao) {
        inicialize(codigo, codigoLiteral, descricao);
    }

    private void inicialize(int codigo, String codigoLiteral, String descricao) {
        this.codigo = codigo;
        this.codigoLiteral = codigoLiteral;
        this.descricao = descricao;
    }

    public static TipoFinalidadeProduto fromCodigo(int codigo) {
        if (lookup.containsKey(codigo))
            return lookup.get(codigo);
        throw new IllegalArgumentException(String.format("Código do Tipo Finalidade Produto inválido: %d", codigo));
    }

    public static TipoFinalidadeProduto of(int codigo) {
        return Stream.of(TipoFinalidadeProduto.values())
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

    public boolean isTipoProduto() {
        return Objects.equals(this, PRODUTO_1);
    }

    public boolean isTipoServico() {
        return Objects.equals(this, SERVICO_2);
    }
}
