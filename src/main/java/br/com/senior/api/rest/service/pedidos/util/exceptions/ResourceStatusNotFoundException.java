package br.com.senior.api.rest.service.pedidos.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceStatusNotFoundException extends RuntimeException {
    public ResourceStatusNotFoundException(String s) {
        super("Resource: ".concat(s).concat(" not found (não encontrado)."));
    }
}
