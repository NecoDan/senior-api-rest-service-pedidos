package br.com.senior.api.rest.service.pedidos.model.pedido;

import br.com.senior.api.rest.service.pedidos.service.descontos.Desconto;
import br.com.senior.api.rest.service.pedidos.util.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@ToString
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ss02_pedido", schema = "senior_services")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pedido extends AbstractEntity {

    @Tolerate
    public Pedido() {
        super();
    }

    @Setter(AccessLevel.NONE)
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "valorItens")
    private BigDecimal valorItens = BigDecimal.ZERO;

    @Setter(AccessLevel.NONE)
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "valor_desconto_total")
    private BigDecimal valorTotalDesconto = BigDecimal.ZERO;

    @Setter(AccessLevel.NONE)
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "valor_total")
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "valor_percentual_desconto")
    private BigDecimal valorPercentualDesconto = BigDecimal.ZERO;

    @Fetch(FetchMode.SELECT)
    // @Transient
    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ItemPedido> itens = new ArrayList<>();

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "id_status_pedido", nullable = false)
    private StatusPedido statusPedido;

    public void add(ItemPedido itemPedido) {
        if (isNotContainsItens())
            this.itens = new ArrayList<>();
        this.itens.add(itemPedido);
    }

    public Pedido gerarId() {
        geraId();
        return this;
    }

    public void addAll(Collection<ItemPedido> itens) {
        this.itens.addAll(itens);
    }

    public void calculaValorTotal() {
        if (isNotContainsItens())
            return;

        this.valorTotal = BigDecimal.valueOf(this.itens
                .stream()
                .filter(Objects::nonNull)
                .filter(ItemPedido::isValorTotalItemInFilterValido)
                .mapToDouble(ItemPedido::getValorTotalItemNumeric)
                .sum())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public void calculaValorItens() {
        if (isNotContainsItens())
            return;

        this.valorItens = BigDecimal.valueOf(this.itens
                .stream()
                .filter(Objects::nonNull)
                .filter(ItemPedido::isValorTotalItemInFilterValido)
                .mapToDouble(ItemPedido::getValorTotalItemNumeric)
                .sum())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public void gerarStatusAberto() {
        this.statusPedido = (StatusPedido.ABERTO_1);
    }

    public void gerarStatusFechado() {
        this.statusPedido = (StatusPedido.FECHADO_2);
    }

    @JsonIgnore
    public boolean isAberto() {
        return (Objects.nonNull(this.statusPedido) && this.statusPedido.isAberto());
    }

    @JsonIgnore
    public boolean isFechado() {
        return (Objects.nonNull(this.statusPedido) && this.statusPedido.isFechado());
    }

    @JsonIgnore
    public boolean isExistemItensPermiteAplicarCalculoDesconto() {
        return (!isNotContainsItens()) && isContemProdutosNosItensPermiteDesconto();
    }

    @JsonIgnore
    public boolean isPermiteAplicarDesconto() {
        return isAberto();
    }

    @JsonIgnore
    private boolean isContemProdutosNosItensPermiteDesconto() {
        return this.itens
                .stream()
                .filter(Objects::nonNull)
                .filter(itemPedido -> Objects.nonNull(itemPedido.getProduto()))
                .anyMatch(itemPedido -> itemPedido.getProduto().isTipoFinalidadeIsProduto());
    }

    public void aplicarCalcularDesconto(Desconto desconto) {
        if (!isPermiteAplicarDesconto() || !isExistemItensPermiteAplicarCalculoDesconto())
            return;

        if (Objects.nonNull(desconto)) {
            this.valorTotalDesconto = desconto.getRegraCalculoPromocaoDesconto().calculaDesconto(this);
            this.valorTotal = this.valorTotal.subtract(this.valorTotalDesconto);
        }

        this.valorTotal = this.valorTotal.setScale(2, RoundingMode.HALF_EVEN);
    }

    @JsonIgnore
    public boolean isNotContainsItens() {
        return Objects.isNull(this.itens) || this.itens.isEmpty();
    }
}
