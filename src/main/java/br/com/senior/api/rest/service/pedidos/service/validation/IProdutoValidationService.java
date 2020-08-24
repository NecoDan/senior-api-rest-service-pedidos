package br.com.senior.api.rest.service.pedidos.service.validation;

import br.com.senior.api.rest.service.pedidos.model.cadastro.Produto;
import br.com.senior.api.rest.service.pedidos.util.exceptions.ServiceException;

public interface IProdutoValidationService {

    void validarSomenteProduto(Produto produto) throws ServiceException;

    void validarIdReferenteProduto(Produto produto) throws ServiceException;

    void validarProduto(Produto produto) throws ServiceException;
}
