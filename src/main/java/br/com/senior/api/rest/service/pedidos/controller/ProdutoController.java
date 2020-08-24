package br.com.senior.api.rest.service.pedidos.controller;


import br.com.senior.api.rest.service.pedidos.model.cadastro.Produto;
import br.com.senior.api.rest.service.pedidos.model.cadastro.TipoFinalidadeProduto;
import br.com.senior.api.rest.service.pedidos.service.negocio.IProdutoService;
import br.com.senior.api.rest.service.pedidos.util.exceptions.ResourceStatusNotFoundException;
import br.com.senior.api.rest.service.pedidos.util.exceptions.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Api(value = "Produto")
public class ProdutoController {

    private final IProdutoService produtoService;
    private static final String DESCRICAO_STATUS_ERRO_INTERNO = "Error";

    @ApiOperation(value = "Retorna todos os produtos existentes com paginação")
    @GetMapping("/produtos")
    public ResponseEntity<Page<Produto>> getAllProdutos(@PageableDefault(page = 0, size = 10, sort = "descricao", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Produto> produtoPage = this.produtoService.recuperarTodos(pageable);
        return (produtoPage.isEmpty()) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<Page<Produto>>(produtoPage, HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna um único produto existente, caso exista, a partir de seu id {UIID} registrado.")
    @GetMapping("/produtos/{id}")
    public ResponseEntity<Produto> getOneProduto(@PathVariable(value = "id") String id) {
        return new ResponseEntity<>(produtoService.recuperarPorId(UUID.fromString(id)).orElseThrow(() -> new ResourceStatusNotFoundException("Produto")), HttpStatus.OK);
    }

    @ApiOperation(value = "Responsável por persistir uma única instância de Produto, a partir de um consumer {Produto} passado como parâmetro no corpo da requisição...")
    @PostMapping("/produtos")
    public ResponseEntity<Produto> saveProduto(@RequestBody @Valid Produto produto) {
        try {
            return new ResponseEntity<>(this.produtoService.salvar(produto), HttpStatus.CREATED);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Responsável por excluir um Produto, a partir de um @PathVariable contendo o id {UIID} do registro.")
    @DeleteMapping("/produtos/{id}")
    public ResponseEntity<?> deleteProduto(@PathVariable(value = "id") String id) {
        try {
            return (produtoService.excluirPor(UUID.fromString(id))) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Responsável por atualizar um Produto, a partir de um @PathVariable contendo o id {UIID} do registro e um consumer {Produto} como corpo da requisição.")
    @PutMapping("/produtos/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable(value = "id") String id,
                                                 @RequestBody @Valid Produto produto) {
        try {
            Optional<Produto> produtoOptional = Optional.of(produtoService.atualizar(UUID.fromString(id), produto));
            return produtoOptional.map(p -> new ResponseEntity<>(p, HttpStatus.ACCEPTED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Retorna todos os produtos, a partir do filtro <b>codigoTipoFinalidadeProduto</b> que estão definidos pelo Tipo Finalidade associado ao produtos.")
    @GetMapping("produtos/buscarPorTipoFinalidade")
    public ResponseEntity<List<Produto>> listAllFromTipoFinalidade(@RequestParam("codigo") @Valid Integer codigo) {
        try {
            return getResponseDefaultFromList(this.produtoService.recuperarTodosPorTipoFinalidade(TipoFinalidadeProduto.fromCodigo(codigo)));
        } catch (ServiceException e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Retorna todos os produtos, a partir do filtro <b>descricao</b> que sejam igual(is) ou semelhante(s) ao passado como parâmetro.")
    @GetMapping("produtos/buscarPorDescricao")
    public ResponseEntity<List<Produto>> listAllFromDescribe(@RequestParam("descricao") @Valid String descricao) {
        try {
            return getResponseDefaultFromList(this.produtoService.recuperarPorDescricao(descricao));
        } catch (ServiceException e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Retorna todos os produtos, a partir do filtro <b>data</b> aos quais foram inseridas. De acordo com o filtro da data passado como parametro no formato {dd/MM/yyyy}.")
    @GetMapping("/produtos/buscarPorDtCadastro")
    public ResponseEntity<List<Produto>> listAllFromDateInsert(@RequestParam("data") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate data) {
        try {
            return getResponseDefaultFromList(this.produtoService.recuperarPorDataCadastro(data));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Retorna todos os produtos, a partir dos filtro(s) <b>Data Inicial</b> e <b>Data Final</b>, que representam o intervalo de datas " +
            " aos quais foram inseridas. O filtro da datas passado como parametro devem ser no formato {dd/MM/yyyy}.")
    @GetMapping("/produtos/buscarPorPeriodo")
    public ResponseEntity<List<Produto>> listAllFromPeriod(@RequestParam("dataInicio") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataInicio,
                                                           @RequestParam("dataFim") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataFim) {
        try {
            return getResponseDefaultFromList(this.produtoService.recuperarTodosPorPeriodo(dataInicio, dataFim));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<List<Produto>> getResponseDefaultFromList(List<Produto> produtoList) {
        return (Objects.isNull(produtoList) || produtoList.isEmpty()) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(produtoList, HttpStatus.OK);
    }
}

