package br.com.senior.api.rest.service.pedidos.model.cadastro;

import br.com.senior.api.rest.service.pedidos.util.domain.AbstractEntity;
import br.com.senior.api.rest.service.pedidos.util.uteis.DecimaUtil;
import br.com.senior.api.rest.service.pedidos.util.uteis.RandomicoUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

@ToString
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ss01_produto", schema = "senior_services")
@Inheritance(strategy = InheritanceType.JOINED)
public class Produto extends AbstractEntity {

    @Tolerate
    public Produto() {
        super();
    }

    @Column(name = "codigoProduto")
    private Long codigoProduto;

    @Column(name = "codigoBarras")
    private String codigoBarras;

    @Size(max = 300, message = "Qtde de caracteres da descrição ultrapassa o valor permitido igual à 300")
    @NotBlank(message = "Insira uma descricao válida para o produto")
    @NotNull(message = "Insira uma descricao válida para o produto")
    @Column(name = "descricao")
    private String descricao;

    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "valor_custo")
    private BigDecimal valorCusto = BigDecimal.ZERO;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "id_tipo_finalidade", nullable = false)
    private TipoFinalidadeProduto tipoFinalidadeProduto;

    @JsonIgnore
    public BigDecimal efetuarGeracaoValorCustoAutomatico() {
        gerarValorCustoAutomatico();
        return this.valorCusto;
    }

    public void gerarValorCustoAutomatico() {
        if (isValorCustoInvalido())
            this.valorCusto = RandomicoUtil.gerarValorRandomicoDecimalAte(1000D);
    }

    public void gerarCodigoBarrasAutomatico() {
        if (Objects.isNull(codigoBarras) || codigoBarras.isEmpty())
            this.codigoBarras = String.valueOf(RandomicoUtil.gerarValorRandomicoLong());
    }

    public void gerarCodigoProdutoAutomatico() {
        if (Objects.isNull(this.codigoProduto) || this.codigoProduto <= 0)
            this.codigoProduto = RandomicoUtil.gerarValorRandomicoLong();
    }

    @JsonIgnore
    public boolean isValorCustoInvalido() {
        return (Objects.isNull(this.valorCusto) || DecimaUtil.isEqualsToZero(this.valorCusto));
    }

    @JsonIgnore
    public boolean isTipoFinalidadeIsProduto() {
        return (Objects.nonNull(tipoFinalidadeProduto) && tipoFinalidadeProduto.isTipoProduto());
    }

    @JsonIgnore
    public boolean isTipoFinalidadeIsServico() {
        return (Objects.nonNull(tipoFinalidadeProduto) && tipoFinalidadeProduto.isTipoServico());
    }

    @JsonIgnore
    public String toStringProduto() {
        return (Objects.isNull(this.getId())) ? this.descricao : (this.getId().toString()) + " - " + this.descricao;
    }

    public void tratarValorCustoProdutoPor(Produto otherProduto) {
        if (isOtherProdutoParamValido(otherProduto) && DecimaUtil.isMaiorQueZero(otherProduto.getValorCusto())) {
            this.valorCusto = otherProduto.getValorCusto();
            return;
        }

        if (!isOtherProdutoParamValido(otherProduto) && !isValorCustoInvalido()) {
            return;
        }

        if (isOtherProdutoParamValido(otherProduto) && isDeveGerarValorAutomaticoProduto(otherProduto)) {
            this.valorCusto = otherProduto.efetuarGeracaoValorCustoAutomatico();
            return;
        }

        this.gerarValorCustoAutomatico();
    }

    private boolean isDeveGerarValorAutomaticoProduto(Produto produtoParam) {
        return (Objects.isNull(produtoParam.getValorCusto()) && Objects.isNull(this.getValorCusto()) || isDeveGerarValorAutomaticoProdutoParamsNotNull(produtoParam));
    }

    private boolean isOtherProdutoParamValido(Produto produtoParam) {
        return (Objects.nonNull(produtoParam));
    }

    private boolean isDeveGerarValorAutomaticoProdutoParamsNotNull(Produto produtoParam) {
        return (isOtherProdutoParamValido(produtoParam) && DecimaUtil.isEqualsToZero(produtoParam.getValorCusto()) || DecimaUtil.isEqualsToZero(this.getValorCusto()));
    }
}
