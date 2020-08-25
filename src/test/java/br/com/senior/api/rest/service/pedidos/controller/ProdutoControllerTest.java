package br.com.senior.api.rest.service.pedidos.controller;

import br.com.senior.api.rest.service.pedidos.model.cadastro.Produto;
import br.com.senior.api.rest.service.pedidos.model.cadastro.TipoFinalidadeProduto;
import br.com.senior.api.rest.service.pedidos.repository.IProdutoRepository;
import br.com.senior.api.rest.service.pedidos.service.negocio.ProdutoService;
import br.com.senior.api.rest.service.pedidos.util.uteis.RandomicoUtil;
import br.com.senior.api.rest.service.pedidos.util.uteis.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    private static final String BASE_URL = "/produtos";
    private static final String URI = BASE_URL + "/{action}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @MockBean
    private IProdutoRepository produtoRepository;

    @Before
    public void setUp() throws Exception {

    }

    private void exibirLogResultPraVerificarRetorno(ResultActions resultActions) throws UnsupportedEncodingException {
        String resultStr = resultActions.andReturn().getResponse().getContentAsString();
        log.info("#TEST_RESULT: ".concat(resultStr));
    }

    @Test
    public void deveRetornarStatus201EProducerJSONAoCriarProdutoMethodPOST() throws Exception {
        log.info("\n#TEST: deveRetornarStatus201EProducerJSONAoCriarProdutoMethodPOST: ");

        // -- 01_Cenário
        ObjectMapper objectMapper = new ObjectMapper();
        Produto produto = constroiProdutoValido();
        produto.setDataCadastro(null);

        // -- 02_Ação
        given(produtoService.salvar(produto)).willReturn(produto);
        ResultActions responseResultActions = this.mockMvc.perform(post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        );

        // -- 03_Verificação_Validação
        responseResultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.descricao").isNotEmpty())
                .andExpect(jsonPath("$.codigoBarras").isNotEmpty())
                .andExpect(jsonPath("$.valorCusto").isNumber())
                .andExpect(jsonPath("$.codigoProduto").isNumber());
        verify(produtoService).salvar(any(Produto.class));

        String statusResponse = String.valueOf(responseResultActions.andReturn().getResponse().getStatus());
        log.info("#TEST_RESULT_STATUS: ".concat((statusResponse.isEmpty()) ? " " : HttpStatus.valueOf(Integer.parseInt(statusResponse)).toString()));
        toStringEnd(responseResultActions, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarStatus202ProducerJSONAoAtualizarProdutoMethodPUT() throws Exception {
        log.info("\n#TEST: deveRetornarStatus202ProducerJSONAoAtualizarProdutoMethodPUT: ");

        // -- 01_Cenário
        ObjectMapper objectMapper = new ObjectMapper();
        Produto produtoParam = constroiProdutoValido();
        produtoParam.setDataCadastro(null);

        UUID idProdutoParam = produtoParam.getId();
        Produto produtoResultUpdate = produtoParam;
        produtoResultUpdate.setValorCusto(RandomicoUtil.gerarValorRandomicoDecimal());

        // -- 02_Ação
        given(produtoService.atualizar(idProdutoParam, produtoParam)).willReturn(produtoResultUpdate);
        ResultActions responseResultActions = this.mockMvc.perform(put(BASE_URL.concat("/" + produtoParam.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoResultUpdate))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        );

        // -- 03_Verificação_Validação
        responseResultActions
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.descricao").isNotEmpty())
                .andExpect(jsonPath("$.codigoBarras").isNotEmpty())
                .andExpect(jsonPath("$.valorCusto").isNumber())
                .andExpect(jsonPath("$.codigoProduto").isNumber());
        verify(produtoService).atualizar(any(UUID.class), any(Produto.class));

        String statusResponse = String.valueOf(responseResultActions.andReturn().getResponse().getStatus());
        log.info("#TEST_RESULT_STATUS: ".concat((statusResponse.isEmpty()) ? " " : HttpStatus.valueOf(Integer.parseInt(statusResponse)).toString()));
        toStringEnd(responseResultActions, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarProducerJSONContendoUmaListaProdutosPorIntervaloDatasMethodGET() throws Exception {
        log.info("\n#TEST: deveRetornarProducerJSONContendoUmaListaProdutosPorIntervaloDatasMethodGET: ");

        // -- 01_Cenário
        LocalDate dataInicio = LocalDate.now();
        LocalDate dataFim = LocalDate.now();

        List<Produto> produtoList = Arrays.asList(constroiProdutoValido(), constroiProdutoValido(), constroiProdutoValido(),
                constroiProdutoValido(), constroiProdutoValido(), constroiProdutoValido(), constroiProdutoValido());

        // -- 02_Ação
        given(produtoService.recuperarTodosPorPeriodo(dataInicio, dataFim)).willReturn(produtoList);
        String uri = BASE_URL.concat("/buscarPorPeriodo?dataInicio=" + StringUtil.formatLocalDate(dataInicio) + "&" + "dataFim=" + StringUtil.formatLocalDate(dataInicio));
        ResultActions responseResultActions = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        responseResultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].dataCadastro").isNotEmpty());
        Assert.assertNotNull(responseResultActions.andReturn().getResponse().getContentAsString());

        toStringEnd(responseResultActions, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarProducerJSONContendoUmaListaProdutosApenasPorUmaDataFiltroInsercaoMethodGET() throws Exception {
        log.info("\n#TEST: deveRetornarProducerJSONContendoUmaListaProdutosApenasPorUmaDataFiltroInsercaoMethodGET: ");

        // -- 01_Cenário
        LocalDate dataFiltro = LocalDate.now();
        List<Produto> produtoList = Arrays.asList(constroiProdutoValido(), constroiProdutoValido());

        // -- 02_Ação
        given(produtoService.recuperarPorDataCadastro(dataFiltro)).willReturn(produtoList);
        String uri = BASE_URL.concat("/buscarPorDtCadastro?data=" + StringUtil.formatLocalDate(dataFiltro));
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].dataCadastro").isNotEmpty());
        Assert.assertNotNull(response.andReturn().getResponse().getContentAsString());

        toStringEnd(response, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarProducerJSONContendoUmaListaProdutosPorFiltroTipoFinalidadeProdMethodGET() throws Exception {
        log.info("\n#TEST: deveRetornarProducerJSONContendoUmaListaProdutosPorFiltroTipoFinalidadeProdMethodGET: ");

        // -- 01_Cenário
        TipoFinalidadeProduto tipoFinalidadeProduto = TipoFinalidadeProduto.PRODUTO_1;
        List<Produto> produtoList = Arrays.asList(constroiProdutoValido(), constroiProdutoValido(), constroiProdutoValido(), constroiProdutoValido());

        // -- 02_Ação
        given(produtoService.recuperarTodosPorTipoFinalidade(tipoFinalidadeProduto)).willReturn(produtoList);
        String uri = BASE_URL.concat("/buscarPorTipoFinalidade?codigo=" + tipoFinalidadeProduto.getCodigo());
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].dataCadastro").isNotEmpty());
        Assert.assertNotNull(response.andReturn().getResponse().getContentAsString());

        toStringEnd(response, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarProducerJSONContendoUmaListaProdutosPorFiltroDescricaoProdMethodGET() throws Exception {
        log.info("\n#TEST: deveRetornarProducerJSONContendoUmaListaProdutosPorFiltroDescricaoProdMethodGET: ");

        // -- 01_Cenário
        String descricao = "Cadeira";
        List<Produto> produtoList = Arrays.asList(constroiProdutoValido(), constroiProdutoValido(), constroiProdutoValido());

        // -- 02_Ação
        given(produtoService.recuperarPorDescricao(descricao)).willReturn(produtoList);
        String uri = BASE_URL.concat("/buscarPorDescricao?descricao=" + descricao);
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].dataCadastro").isNotEmpty())
                .andExpect(jsonPath("$.[*].dataCadastro").isArray());
        Assert.assertNotNull(response.andReturn().getResponse().getContentAsString());

        toStringEnd(response, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarPeloMenosUnicoProduto() throws Exception {
        log.info("\n#TEST: deveRetornarPeloMenosUnicoProduto: ");

        // -- 01_Cenário
        UUID idProduto = UUID.randomUUID();
        Produto produto = constroiProdutoValido();

        // -- 02_Ação
        given(produtoService.recuperarPorId(idProduto)).willReturn(Optional.of(produto));
        String uri = BASE_URL.concat("/").concat(idProduto.toString());
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.valorCusto").isNotEmpty())
                .andExpect(jsonPath("$.valorCusto").isNumber())
                .andExpect(jsonPath("$.codigoProduto").isNumber());
        Assert.assertNotNull(response.andReturn().getResponse().getContentAsString());

        toStringEnd(response, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarErrorComStatus400EGerarMensagemAoNaoEncontrarResourceEndpointGetProdutosPorDescricao() throws Exception {
        log.info("\n#TEST: deveRetornarErrorComStatus400EGerarMensagemAoNaoEncontrarResourceEndpointGetProdutosPorDescricao: ");

        // -- 01_Cenário
        String resultReponseServer = "Responser server: Nenhum pedido(s) encontrado.";

        // -- 02_Ação
        given(produtoService.recuperarPorDescricao("")).willReturn(null);
        String uri = BASE_URL.concat("/buscarPorDescricao?descricao=" + "");
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isNotFound());
        String result = response.andReturn().getResponse().getContentAsString();
        assertTrue(Objects.nonNull(result) || !result.isEmpty() && result.contains(resultReponseServer));

        log.info("#TEST_RESULT: ".concat(result));
        log.info("-------------------------------------------------------------");
    }

    @Test
    public void deveRetornarErrorComStatus400EGerarMensagemAoNaoEncontrarResourceEndpointGetProdutosPorData() throws Exception {
        log.info("\n#TEST: deveRetornarErrorComStatus400EGerarMensagemAoNaoEncontrarResourceEndpointGetProdutosPorData: ");

        // -- 01_Cenário
        String resultReponseServer = "Responser server: Nenhum pedido(s) encontrado.";

        // -- 02_Ação
        given(produtoService.recuperarPorDataCadastro(null)).willReturn(null);
        String uri = BASE_URL.concat("/buscarPorDtCadastro?data=" + "");
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isNotFound());
        String result = response.andReturn().getResponse().getContentAsString();
        assertTrue(Objects.nonNull(result) || !result.isEmpty() && result.contains(resultReponseServer));

        log.info("#TEST_RESULT: ".concat(result));
        log.info("-------------------------------------------------------------");
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
        produto.ativado();
        produto.gerarDataCorrente();
        return produto;
    }

    private void toStringEnd(ResultActions response, MediaType mediaType) throws Exception {
        if (Objects.isNull(response) || Objects.isNull(mediaType)) {
            log.info("#TEST_RESULT: ".concat("Error ao gerar saida. Não existem dados..."));
            log.info("-------------------------------------------------------------");
            return;
        }

        String result = response.andReturn().getResponse().getContentAsString();
        String out = "";

        if (mediaType == MediaType.APPLICATION_JSON)
            out = StringUtil.formatConteudoJSONFrom(result);

        if (mediaType == MediaType.APPLICATION_XML)
            out = StringUtil.formatConteudoXMLFrom(result);

        log.info("#TEST_RESULT: ".concat(out));
        log.info("-------------------------------------------------------------");
    }

    private ResultActions getResponseEntityEndPointsMethodGET(String url, MediaType mediaType) throws Exception {
        return this.mockMvc.perform(get(url).accept(mediaType));
    }
}
