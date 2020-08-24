package br.com.senior.api.rest.service.pedidos.model.pedido;

import br.com.senior.api.rest.service.pedidos.model.cadastro.Produto;
import br.com.senior.api.rest.service.pedidos.util.domain.AbstractEntity;
import br.com.senior.api.rest.service.pedidos.util.uteis.DecimaUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ss03_item_pedido", schema = "senior_services")
@Inheritance(strategy = InheritanceType.JOINED)
public class ItemPedido extends AbstractEntity {

    @Tolerate
    public ItemPedido() {
        super();
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @OneToOne
    @JoinColumn(name = "id_produto")
    private Produto produto;

    @Column(name = "item")
    private Integer item;

    @Min(1)
    @PositiveOrZero
    @DecimalMin(value = "1.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "qtde")
    private BigDecimal quantidade;

    @Setter(AccessLevel.NONE)
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "valor_item")
    private BigDecimal valorItem;

    @Setter(AccessLevel.NONE)
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto;

    @JsonIgnore
    public BigDecimal getValorTotalCalculado() {
        calculaValorTotal();
        return this.valorTotal;
    }

    @JsonIgnore
    public BigDecimal getQuantidadeGarantida() {
        this.quantidade = (Objects.isNull(this.quantidade)) ? BigDecimal.ONE : this.quantidade;
        return this.quantidade;
    }

    public void calculaValorTotal() {
        this.inicializarParamsEfetuarCalculo();
        this.valorTotal = calculaValorTotalItem();
    }

    public BigDecimal calculaValorTotalItem() {
        if (Objects.isNull(this.valorItem) || DecimaUtil.isMenorOuIgualAZero(this.valorItem))
            aplicarValorItemAoValorCustoProduto();
        return this.valorItem.multiply(getQuantidadeGarantida());
    }

    public void aplicarValorItemAoValorCustoProduto() {
        if (Objects.nonNull(this.produto))
            this.valorItem = this.produto.getValorCusto().setScale(2, RoundingMode.HALF_UP);
    }

    @JsonIgnore
    public boolean isValorTotalItemInFilterValido() {
        return Objects.nonNull(this.valorTotal);
    }

    @JsonIgnore
    public boolean isValorItemInFilterValido() {
        return Objects.nonNull(this.valorItem);
    }

    @JsonIgnore
    public boolean isValorDescontoItemInFilterValido() {
        return Objects.nonNull(this.valorDesconto);
    }

    @JsonIgnore
    public double getValorTotalItemNumeric() {
        return (isValorTotalItemInFilterValido()) ? this.valorTotal.doubleValue() : BigDecimal.ZERO.doubleValue();
    }

    @JsonIgnore
    public double getValorItemNumeric() {
        return (isValorItemInFilterValido()) ? this.valorItem.doubleValue() : BigDecimal.ZERO.doubleValue();
    }

    @JsonIgnore
    public double getValorDescontoItemNumeric() {
        return (isValorDescontoItemInFilterValido()) ? this.valorDesconto.doubleValue() : BigDecimal.ZERO.doubleValue();
    }

    private void inicializarParamsEfetuarCalculo() {
        if (Objects.isNull(this.valorTotal))
            this.valorTotal = BigDecimal.ZERO;

        if (Objects.isNull(this.valorItem))
            this.valorItem = BigDecimal.ZERO;

        if (Objects.isNull(this.valorDesconto))
            this.valorDesconto = BigDecimal.ZERO;
    }
}
