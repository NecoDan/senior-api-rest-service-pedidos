package br.com.senior.api.rest.service.pedidos.service.validation;

import br.com.senior.api.rest.service.pedidos.model.pedido.ItemPedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.util.exceptions.ServiceException;
import br.com.senior.api.rest.service.pedidos.util.uteis.DecimaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class PedidoValidationService implements IPedidoValidationService {

    @Override
    public void validarSomentePedido(Pedido pedido) throws ServiceException {
        if (Objects.isNull(pedido))
            throw new ServiceException("Nenhum Pedido encontrado. Pedido, encontra-se inválido e/ou inexistente {NULL}.");
    }

    @Override
    public void validarIdReferentePedido(Pedido pedido) throws ServiceException {
        if (Objects.nonNull(pedido) && Objects.isNull(pedido.getId()))
            throw new ServiceException("Nenhum Pedido encontrao. Pedido encontra-se com a referência do [ID] inválida e/ou inexistente {NULL}.");
    }

    @Override
    public void validarSomenteItemPedido(ItemPedido itemPedido) throws ServiceException {
        if (Objects.isNull(itemPedido))
            throw new ServiceException("Nenhum item do pedido encontrado. O Item contido no Pedido encontra-se inválido e/ou inexistente {NULL}.");
    }

    @Override
    public void validarIdReferenteItemPedido(ItemPedido itemPedido) throws ServiceException {
        if (Objects.isNull(itemPedido.getId()))
            throw new ServiceException("Nenhum item do pedido encontrado. O Item contido no Pedido encontra-se com a referência do {ID} inválida e/ou inexistente {NULL}.");
    }

    @Override
    public void validarParamsEDependenciasPedido(Pedido pedido) throws ServiceException {
        validarSomentePedido(pedido);

        if (Objects.isNull(pedido.getItens()) || pedido.getItens().isEmpty())
            throw new ServiceException("Nenhum item encontrado ou adicionado ao Pedido. Encontram-se inválido(s) e/ou inexistente(s). Adicione um item e tente novamente...");

        if (Objects.nonNull(pedido.getValorPercentualDesconto()) && DecimaUtil.isMenorQueZero(pedido.getValorPercentualDesconto()))
            throw new ServiceException("O valor de (%) percentual de desconto não deve ser um valor menor que zero (0).");
    }
}
