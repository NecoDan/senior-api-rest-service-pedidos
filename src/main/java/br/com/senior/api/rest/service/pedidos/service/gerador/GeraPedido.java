package br.com.senior.api.rest.service.pedidos.service.gerador;

import br.com.senior.api.rest.service.pedidos.model.cadastro.Produto;
import br.com.senior.api.rest.service.pedidos.model.pedido.ItemPedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.service.descontos.Desconto;
import br.com.senior.api.rest.service.pedidos.service.negocio.IPedidoService;
import br.com.senior.api.rest.service.pedidos.service.negocio.IProdutoService;
import br.com.senior.api.rest.service.pedidos.service.strategy.BuilderDescontoService;
import br.com.senior.api.rest.service.pedidos.service.validation.IPedidoValidationService;
import br.com.senior.api.rest.service.pedidos.util.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeraPedido implements IGeraPedido {

    private final IPedidoService pedidoService;
    private final IProdutoService produtoService;
    private final IPedidoValidationService pedidoValidationService;
    private final BuilderDescontoService builderDescontoService;

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public Pedido atualizarPedido(UUID idPedido, Pedido pedido) throws ServiceException {
        this.pedidoValidationService.validarSomentePedido(pedido);
        pedido.setId(idPedido);

        this.pedidoValidationService.validarIdReferentePedido(pedido);
        Pedido pedidoUpdate = getPedidoExistente(pedido);
        this.pedidoValidationService.validarSomentePedido(pedidoUpdate);

        pedidoUpdate.setStatusPedido(Objects.isNull(pedido.getStatusPedido()) ? pedidoUpdate.getStatusPedido() : pedido.getStatusPedido());
        pedidoUpdate.setValorPercentualDesconto(Objects.isNull(pedido.getValorPercentualDesconto()) ? pedidoUpdate.getValorPercentualDesconto() : pedido.getValorPercentualDesconto());
        efetuarGeracaoPedido(pedidoUpdate, true);
        return pedidoUpdate;
    }

    private ItemPedido montarItemPedidoFromOther(ItemPedido itemPedidoOrigin, ItemPedido itemPedidoDestino) {
//        itemPedidoDestino.setPedido(Objects.isNull()itemPedidoOrigin.getPedido());
//        itemPedidoDestino.setProduto(itemPedidoOrigin.);
//
        return itemPedidoDestino;
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public Pedido gerar(Pedido pedidoParam) throws ServiceException {
        this.pedidoValidationService.validarSomentePedido(pedidoParam);
        Pedido pedidoNovo = getPedidoExistente(pedidoParam);

        if (Objects.nonNull(pedidoNovo) && Objects.nonNull(pedidoNovo.getId()))
            return pedidoNovo;

        this.pedidoValidationService.validarParamsEDependenciasPedido(pedidoNovo);
        this.validarProdutosItensPedido(pedidoNovo.getItens());
        this.efetuarGeracaoItensPedido(pedidoNovo, pedidoNovo.getItens());

        pedidoNovo.geraId();
        pedidoNovo.gerarStatusAberto();
        pedidoNovo.gerarDataCorrente();
        pedidoNovo.ativado();

        efetuarGeracaoPedido(pedidoNovo, true);
        return pedidoNovo;
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public void efetuarGeracaoItensPedido(Pedido pedido, List<ItemPedido> itens) throws ServiceException {
        int item = 1;
        for (ItemPedido itemPedido : itens) {
            gerarItemPedido(item, itemPedido);
            item++;
        }
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public void efetuarGeracaoPedido(Pedido pedidoNovo, boolean calculaDesconto) throws ServiceException {
        pedidoNovo.calculaValorTotal();

        if (calculaDesconto) {
            Desconto desconto = this.builderDescontoService.obterDescontoAPartir(pedidoNovo);
            pedidoNovo.aplicarCalcularDesconto(desconto);
        }

        pedidoNovo.calculaValorItens();
        pedidoNovo.gerarStatusFechado();
        pedidoNovo.gerarDataCorrente();
        this.pedidoService.salvar(pedidoNovo);

        List<ItemPedido> itensPedidoAtualizar = pedidoNovo.getItens();
        itensPedidoAtualizar.forEach(itemPedido -> {
            try {
                salvarItem(itemPedido, pedidoNovo);
            } catch (ServiceException e) {
                log.error(e.getLocalizedMessage());
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public ItemPedido gerarItemPedido(Integer item, ItemPedido itemPedido) throws ServiceException {
        Produto produto = this.produtoService.recuperarPorId(itemPedido.getProduto().getId()).orElse(null);

        itemPedido.geraId();
        itemPedido.setItem(item);
        itemPedido.setProduto(produto);
        itemPedido.getQuantidadeGarantida();
        itemPedido.gerarDataCorrente();
        itemPedido.ativado();
        return processarItem(itemPedido);
    }

    @Transactional
    protected ItemPedido processarItem(ItemPedido itemPedido) throws ServiceException {
        itemPedido.calculaValorTotal();
        itemPedido.setValorDesconto(BigDecimal.ZERO);
        itemPedido = this.pedidoService.salvarItem(itemPedido);
        return itemPedido;
    }

    @Transactional
    public void salvarItem(ItemPedido itemPedido, Pedido pedido) throws ServiceException {
        itemPedido.setPedido(pedido);
        this.pedidoService.salvarItem(itemPedido);
    }

    @Override
    public void validarProdutosItensPedido(List<ItemPedido> itemPedidoList) throws ServiceException {
        if (Objects.isNull(itemPedidoList) || itemPedidoList.isEmpty())
            throw new ServiceException("Nenhum item relacionado aos Item(ns) do Pedido encontrado. Encontram-se inválidos {NULL} e/ou itens não foram definidos ao Pedido.");

        List<Produto> produtoList = itemPedidoList.stream().filter(Objects::nonNull).filter(itemPedido -> Objects.nonNull(itemPedido.getProduto()))
                .map(ItemPedido::getProduto).collect(Collectors.toList());

        if (produtoList.isEmpty())
            throw new ServiceException("Nenhum produto relacionado aos Item(ns) do Pedido encontrado. Encontram-se inválidos {NULL} e/ou produtos não foram definidos.");

        Map<String, String> mapProdutosDesativadosAoGerarPedido = new HashMap<>();

        for (Produto p : produtoList) {
            Produto produto = this.produtoService.recuperarPorId(p.getId()).orElse(null);

            if (Objects.isNull(produto)) {
                Optional<ItemPedido> produtoItemPedido = itemPedidoList.stream().filter(itemPedido -> itemPedido.getProduto().getId().equals(p.getId())).findFirst();
                produtoItemPedido.ifPresent(itemPedido -> mapProdutosDesativadosAoGerarPedido.put(String.valueOf(itemPedido.getProduto().getId()), "Produto não encontrado."));
            }

            if (Objects.nonNull(produto) && !produto.isAtivo())
                mapProdutosDesativadosAoGerarPedido.put(String.valueOf(produto.toStringProduto()), "Produto encontra-se desativado.");
        }

        if (!mapProdutosDesativadosAoGerarPedido.isEmpty()) {
            throw new ServiceException("Algum(ns) produto(s) adicionado(s) ao Pedido, encontram-se desativados(s) e/ou inválidos."
                    + "Não é permitido adicionar um produto desativado ao pedido: "
                    + getConteudoMensagemFromMapProdutosInvalidos(mapProdutosDesativadosAoGerarPedido));
        }
    }

    private Pedido getPedidoExistente(Pedido pedido) {
        Pedido pedidoExistente = null;

        if (Objects.nonNull(pedido) && Objects.nonNull(pedido.getId())) {
            pedidoExistente = this.pedidoService.recuperarPorId(pedido.getId()).orElse(null);
        }

        return (Objects.isNull(pedidoExistente)) ? pedido : pedidoExistente;
    }

    private String getConteudoMensagemFromMapProdutosInvalidos(Map<String, String> map) {
        return map.keySet()
                .stream()
                .map(key -> key + " : " + map.get(key))
                .collect(Collectors.joining(", ", "\n", ""));
    }
}
