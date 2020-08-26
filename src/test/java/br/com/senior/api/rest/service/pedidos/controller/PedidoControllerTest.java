package br.com.senior.api.rest.service.pedidos.controller;

import br.com.senior.api.rest.service.pedidos.model.cadastro.Produto;
import br.com.senior.api.rest.service.pedidos.model.cadastro.TipoFinalidadeProduto;
import br.com.senior.api.rest.service.pedidos.model.pedido.ItemPedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.senior.api.rest.service.pedidos.model.pedido.StatusPedido;
import br.com.senior.api.rest.service.pedidos.service.gerador.GeraPedidoService;
import br.com.senior.api.rest.service.pedidos.service.negocio.PedidoService;
import br.com.senior.api.rest.service.pedidos.util.uteis.RandomicoUtil;
import br.com.senior.api.rest.service.pedidos.util.uteis.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    private static final String BASE_URL = "/pedidos";
    private static final String URI = BASE_URL + "/{action}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @MockBean
    private GeraPedidoService geraPedido;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:dd"));
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        this.objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    @Test
    public void deveRetornarStatus201EProducerJSONAoCriarPedidoMethodPOST() throws Exception {
        log.info("\n#TEST: deveRetornarStatus201EProducerJSONAoCriarPedidoMethodPOST: ");

        // -- 01_Cenário
        Pedido pedido = constroiPedidoValido();

        // -- 02_Ação
        given(geraPedido.gerar(pedido)).willReturn(pedido);
        ResultActions responseResultActions = this.mockMvc.perform(post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .content(getJsonValuePedidoFromPedidoObj(pedido))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        );

        // -- 03_Verificação_Validação
        responseResultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.statusPedido").isNotEmpty())
                .andExpect(jsonPath("$.valorItens").isNumber())
                .andExpect(jsonPath("$.valorTotal").isNumber())
                .andExpect(jsonPath("$.valorPercentualDesconto").isNumber());
        verify(geraPedido).gerar(any(Pedido.class));

        String statusResponse = String.valueOf(responseResultActions.andReturn().getResponse().getStatus());
        log.info("#TEST_RESULT_STATUS: ".concat((statusResponse.isEmpty()) ? " " : HttpStatus.valueOf(Integer.parseInt(statusResponse)).toString()));
        toStringEnd(responseResultActions);
    }

    @Test
    public void deveRetornarProducerJSONContendoUmaListaPedidosPorFiltroStatusPedidoMethodGET() throws Exception {
        log.info("\n#TEST: deveRetornarProducerJSONContendoUmaListaPedidosPorFiltroStatusPedidoMethodGET: ");

        // -- 01_Cenário
        StatusPedido statusPedido = StatusPedido.ABERTO_1;
        List<Pedido> pedidoList = Arrays.asList(constroiPedidoValido(), constroiPedidoValido(), constroiPedidoValido(), constroiPedidoValido(), constroiPedidoValido());

        // -- 02_Ação
        given(pedidoService.recuperarTodosPorStatus(statusPedido)).willReturn(pedidoList);
        String uri = BASE_URL.concat("/buscarPorStatus?codigo=" + statusPedido.getCodigo());
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].dataCadastro").isNotEmpty())
                .andExpect(jsonPath("$.[*].valorItens").isNotEmpty())
                .andExpect(jsonPath("$.[*].valorTotal").isNotEmpty())
                .andExpect(jsonPath("$.[*].statusPedido").isNotEmpty());
        Assert.assertNotNull(response.andReturn().getResponse().getContentAsString());

        toStringEnd(response);
    }

    @Test
    public void deveRetornarProducerJSONContendoUmaListaPedidosPorIntervaloDatasMethodGET() throws Exception {
        log.info("\n#TEST: deveRetornarProducerJSONContendoUmaListaPedidosPorIntervaloDatasMethodGET: ");

        // -- 01_Cenário
        LocalDate dataInicio = LocalDate.now();
        LocalDate dataFim = LocalDate.now();

        List<Pedido> pedidoList = Arrays.asList(constroiPedidoValido(), constroiPedidoValido(), constroiPedidoValido(), constroiPedidoValido());

        // -- 02_Ação
        given(pedidoService.recuperarTodosPorPeriodoDtCadastro(dataInicio, dataFim)).willReturn(pedidoList);
        String uri = BASE_URL.concat("/buscarPorPeriodo?dataInicio=" + StringUtil.formatLocalDate(dataInicio) + "&" + "dataFim=" + StringUtil.formatLocalDate(dataInicio));
        ResultActions responseResultActions = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        responseResultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].dataCadastro").isNotEmpty())
                .andExpect(jsonPath("$.[*].valorItens").isNotEmpty())
                .andExpect(jsonPath("$.[*].valorTotal").isNotEmpty());
        assertNotNull(responseResultActions.andReturn().getResponse().getContentAsString());

        toStringEnd(responseResultActions);
    }

    private Pedido constroiPedidoValido() {
        Pedido pedido = Pedido.builder()
                .statusPedido(StatusPedido.ABERTO_1)
                .valorItens(RandomicoUtil.gerarValorRandomicoDecimal())
                .valorTotal(RandomicoUtil.gerarValorRandomicoDecimal())
                .valorPercentualDesconto(RandomicoUtil.gerarValorRandomicoDecimal())
                .build()
                .gerarId();

        pedido.ativado();
        pedido.gerarDataCorrente();

        ItemPedido itemPedido = ItemPedido.builder()
                .produto(constroiProdutoValido())
                .quantidade(RandomicoUtil.gerarValorRandomicoDecimal())
                .build();

        pedido.add(itemPedido);
        return pedido;
    }

    private Produto constroiProdutoValido() {
        Produto produto = Produto.builder()
                .valorCusto(RandomicoUtil.gerarValorRandomicoDecimal())
                .descricao(String.valueOf(RandomicoUtil.gerarValorRandomicoLong()))
                .tipoFinalidadeProduto(TipoFinalidadeProduto.PRODUTO_1)
                .build()
                .geraValorCustoAutomatico()
                .geraCodigoProdutoAutomatico()
                .geraCodigoBarrasAutomatico();

        produto.geraId();
        produto.gerarDataCorrente();
        produto.ativado();

        return produto;
    }

    private ResultActions getResponseEntityEndPointsMethodGET(String url, MediaType mediaType) throws Exception {
        return this.mockMvc.perform(get(url).accept(mediaType));
    }

    private String getJsonValuePedidoFromPedidoObj(Pedido pedido) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(pedido);
    }

    private void toStringEnd(ResultActions response) throws Exception {
        if (Objects.isNull(response)) {
            log.info("#TEST_RESULT: ".concat("Error ao gerar saida. Não existem dados..."));
            log.info("-------------------------------------------------------------");
            return;
        }

        String result = response.andReturn().getResponse().getContentAsString();
        String out = StringUtil.formatConteudoJSONFrom(result);

        log.info("#TEST_RESULT: ".concat(out));
        log.info("-------------------------------------------------------------");
    }
}
