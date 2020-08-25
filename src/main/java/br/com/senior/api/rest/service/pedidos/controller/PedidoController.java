package br.com.senior.api.rest.service.pedidos.controller;

import br.com.senior.api.rest.service.pedidos.model.pedido.ItemPedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.StatusPedido;
import br.com.senior.api.rest.service.pedidos.service.gerador.IGeraPedido;
import br.com.senior.api.rest.service.pedidos.service.negocio.IPedidoService;
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
@Api(value = "Pedido")
public class PedidoController {

    private final IPedidoService pedidoService;
    private final IGeraPedido geraPedido;

    @ApiOperation(value = "Retorna todos os produtos existentes paginável.")
    @GetMapping("/pedidos")
    public ResponseEntity<Page<Pedido>> getAllPedidos(@PageableDefault(page = 0, size = 10, sort = "dataCadastro", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Pedido> produtoPage = this.pedidoService.recuperarTodos(pageable);
        return (produtoPage.isEmpty()) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<Page<Pedido>>(produtoPage, HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna um único pedido existente, caso exista, a partir de seu id {UIID} registrado.")
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<Pedido> getOnePedido(@PathVariable(value = "id") String id) {
        Optional<Pedido> optionalPedido = this.pedidoService.recuperarPorId(UUID.fromString(id));
        return optionalPedido.map(pedido -> new ResponseEntity<>(pedido, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Responsável por persistir um Pedido, a partir de um consumer {Pedido} passado como parâmetro no corpo da requisição...")
    @PostMapping("/pedidos")
    public ResponseEntity<Pedido> savePedido(@RequestBody @Valid Pedido pedido) {
        try {
            return new ResponseEntity<>(this.geraPedido.gerar(pedido), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Responsável por excluir um Pedido, a partir de um @PathVariable contendo o id {UIID} do registro.")
    @DeleteMapping("/pedidos/{id}")
    public ResponseEntity<?> deletePedido(@PathVariable(value = "id") String id) {
        try {
            return (pedidoService.excluirPor(UUID.fromString(id))) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Responsável por atualizar um Pedido, a partir de um @PathVariable contendo o id {UIID} do registro e um consumer {Pedido} como corpo da requisição.")
    @PutMapping("/pedidos/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable(value = "id") String id,
                                               @RequestBody @Valid Pedido pedido) {
        try {
            if (!pedido.getItens().isEmpty()) {
                String mensagemValidacaoItens = "Não é possível atualizar e/ou adicionar itens ao pedido por meio desta URI. URI disponiveis: \n"
                        + " * Para Atualzar Item: {METHOD_PUT} http://localhost:8080/pedidos/itemPedido/" + "\n"
                        + " * Para Adicionar Item: {METHOD_POST} http://localhost:8080/pedidos/itemPedido/adicionarItem" + "\n";
                log.error(mensagemValidacaoItens);
                return new ResponseEntity(mensagemValidacaoItens, HttpStatus.BAD_REQUEST);
            }

            Optional<Pedido> produtoOptional = Optional.of(geraPedido.atualizarPedido(UUID.fromString(id), pedido));
            return produtoOptional.map(p -> new ResponseEntity<>(p, HttpStatus.ACCEPTED)).orElseGet(() -> new ResponseEntity("Pedido não encontrado", HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Retorna todos os pedidos, a partir do filtro <b>codigo</b> referente ao Status Pedido aos quais estão associado ao(s) pedidos.")
    @GetMapping("pedidos/buscarPorStatus")
    public ResponseEntity<List<Pedido>> listAllFromStatusPedido(@RequestParam("codigo") @Valid Integer codigo) {
        try {
            return getResponseDefaultFromList(this.pedidoService.recuperarTodosPorStatus(StatusPedido.fromCodigo(codigo)));
        } catch (ServiceException e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Responsável por atualizar um item do Pedido, a partir de um @PathVariable contendo o id {UIID} do registro e um consumer {ItemPedido} como corpo da requisição.")
    @PutMapping("/pedidos/itemPedido/{id}")
    public ResponseEntity<ItemPedido> updateItemPedido(@PathVariable(value = "id") String id,
                                                       @RequestBody @Valid ItemPedido itemPedido) {
        return new ResponseEntity<ItemPedido>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Retorna todos os pedidos, a partir do filtro <b>data</b> aos quais foram inseridas. De acordo com o filtro da data passado como parametro no formato {dd/MM/yyyy}.")
    @GetMapping("/pedidos/buscarPorDtCadastro")
    public ResponseEntity<List<Pedido>> listAllFromDateInsert(@RequestParam("data") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate data) {
        try {
            return getResponseDefaultFromList(this.pedidoService.recuperarTodosPorDataCadastro(data));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Retorna todos os pedidos, a partir dos filtro(s) <b>Data Inicial</b> e <b>Data Final</b>, que representam o intervalo de datas " +
            " aos quais foram inseridas. O filtro da datas passado como parametro devem ser no formato {dd/MM/yyyy}.")
    @GetMapping("/pedidos/buscarPorPeriodo")
    public ResponseEntity<List<Pedido>> listAllFromPeriod(@RequestParam("dataInicio") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataInicio,
                                                          @RequestParam("dataFim") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataFim) {
        try {
            return getResponseDefaultFromList(this.pedidoService.recuperarTodosPorPeriodoDtCadastro(dataInicio, dataFim));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<List<Pedido>> getResponseDefaultFromList(List<Pedido> pedidoList) {
        return (Objects.isNull(pedidoList) || pedidoList.isEmpty()) ? new ResponseEntity("Responser server: Nenhum pedido(s) encontrado.", HttpStatus.NOT_FOUND) : new ResponseEntity<>(pedidoList, HttpStatus.OK);
    }
}
