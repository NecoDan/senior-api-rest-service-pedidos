package br.com.senior.api.rest.service.pedidos.service.validation;

import br.com.senior.api.rest.service.pedidos.model.cadastro.Produto;
import br.com.senior.api.rest.service.pedidos.util.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class ProdutoValidationService implements IProdutoValidationService {

    @Override
    public void validarSomenteProduto(Produto produto) throws ServiceException {
        if (Objects.isNull(produto))
            throw new ServiceException("Produto encontra-se inválido e/ou inexistente {NULL}. Nenhuma referência do objeto {PRODUTO} localizada.");
    }

    @Override
    public void validarIdReferenteProduto(Produto produto) throws ServiceException {
        validarSomenteProduto(produto);

        if (Objects.isNull(produto.getId()))
            throw new ServiceException("Produto encontra-se com a referência do [ID] inválida e/ou inexistente {NULL}. Nenhuma referência do objeto {ID_PRODUTO} localizada.");
    }

    @Override
    public void validarProduto(Produto produto) throws ServiceException {
        validarSomenteProduto(produto);
        validarIdReferenteProduto(produto);

        if (Objects.isNull(produto.getDescricao()) || produto.getDescricao().isEmpty())
            throw new ServiceException("Descrição referente ao Produto encontra-se inválida (vazia) e/ou inexistente {NULL}.");
    }
}
